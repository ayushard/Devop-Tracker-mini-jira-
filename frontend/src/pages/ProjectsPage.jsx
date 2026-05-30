import { useEffect, useState } from 'react';
import EmptyState from '../components/shared/EmptyState';
import Loader from '../components/shared/Loader';
import PageHeader from '../components/shared/PageHeader';
import Pagination from '../components/shared/Pagination';
import { useAuth } from '../hooks/useAuth';
import { projectService } from '../services/projectService';
import { formatDate } from '../utils/formatters';

const initialForm = {
  name: '',
  description: '',
  keyCode: '',
  startDate: '',
  endDate: '',
};

function ProjectsPage() {
  const { isAdmin } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [projects, setProjects] = useState({ content: [], page: 0, totalPages: 0 });
  const [search, setSearch] = useState('');
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);

  const loadProjects = async (page = 0) => {
    setLoading(true);
    try {
      setProjects(await projectService.getProjects({ page, size: 6, search }));
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProjects();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      if (editingId) {
        await projectService.updateProject(editingId, form);
      } else {
        await projectService.createProject(form);
      }
      setForm(initialForm);
      setEditingId(null);
      await loadProjects(projects.page);
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Projects"
        title="Manage delivery initiatives"
        description="Create initiatives, track task completion, and keep project timelines visible to the whole team."
        action={
          <button className="btn-secondary" onClick={() => loadProjects(0)}>
            Refresh
          </button>
        }
      />

      <div className="grid gap-4 xl:grid-cols-[1.2fr_0.8fr]">
        <div className="glass-panel p-6">
          <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
            <input className="input md:max-w-sm" value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search projects" />
            <button className="btn-primary" onClick={() => loadProjects(0)}>Search</button>
          </div>

          <div className="mt-6 space-y-4">
            {loading ? (
              <Loader label="Loading projects..." />
            ) : projects.content.length === 0 ? (
              <EmptyState title="No projects found" description="Create the first project or adjust your search query." />
            ) : (
              projects.content.map((project) => (
                <div key={project.id} className="rounded-3xl border border-slate-100 bg-white p-5">
                  <div className="flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
                    <div>
                      <div className="flex items-center gap-3">
                        <h3 className="text-lg font-bold text-ink">{project.name}</h3>
                        <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-600">{project.keyCode}</span>
                      </div>
                      <p className="mt-2 text-sm text-slate-500">{project.description}</p>
                    </div>
                    {isAdmin && (
                      <div className="flex gap-2">
                        <button className="btn-secondary" onClick={() => {
                          setEditingId(project.id);
                          setForm({
                            name: project.name,
                            description: project.description || '',
                            keyCode: project.keyCode,
                            startDate: project.startDate || '',
                            endDate: project.endDate || '',
                          });
                        }}>
                          Edit
                        </button>
                        <button className="btn-secondary" onClick={async () => {
                          await projectService.deleteProject(project.id);
                          await loadProjects(projects.page);
                        }}>
                          Delete
                        </button>
                      </div>
                    )}
                  </div>

                  <div className="mt-5 grid gap-3 text-sm text-slate-500 md:grid-cols-3">
                    <p>Start: {formatDate(project.startDate)}</p>
                    <p>End: {formatDate(project.endDate)}</p>
                    <p>
                      Progress: {project.completedTasks}/{project.totalTasks} tasks
                    </p>
                  </div>
                </div>
              ))
            )}
          </div>

          <Pagination page={projects.page} totalPages={projects.totalPages} onChange={loadProjects} />
        </div>

        <div className="glass-panel p-6">
          <p className="text-sm font-semibold text-slate-500">Project workspace</p>
          <h3 className="mt-1 text-xl font-bold text-ink">{editingId ? 'Edit project' : 'Create project'}</h3>
          {!isAdmin ? (
            <p className="mt-4 text-sm text-slate-500">Project creation is available to admins only.</p>
          ) : (
            <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
              <input className="input" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder="Project name" />
              <input className="input" value={form.keyCode} onChange={(e) => setForm({ ...form, keyCode: e.target.value })} placeholder="Key code" />
              <textarea className="input min-h-28" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} placeholder="Description" />
              <input className="input" type="date" value={form.startDate} onChange={(e) => setForm({ ...form, startDate: e.target.value })} />
              <input className="input" type="date" value={form.endDate} onChange={(e) => setForm({ ...form, endDate: e.target.value })} />
              {error && <p className="text-sm text-rose-600">{error}</p>}
              <button className="btn-primary w-full" disabled={saving}>
                {saving ? 'Saving...' : editingId ? 'Update project' : 'Create project'}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}

export default ProjectsPage;
