import { useEffect, useState } from 'react';
import EmptyState from '../components/shared/EmptyState';
import Loader from '../components/shared/Loader';
import PageHeader from '../components/shared/PageHeader';
import Pagination from '../components/shared/Pagination';
import { useAuth } from '../hooks/useAuth';
import { projectService } from '../services/projectService';
import { taskService } from '../services/taskService';
import { userService } from '../services/userService';
import { formatDate, getPriorityBadge, getStatusBadge } from '../utils/formatters';

const initialTaskForm = {
  title: '',
  description: '',
  priority: 'MEDIUM',
  status: 'TODO',
  deadline: '',
  projectId: '',
  assignedUserId: '',
};

function TasksPage() {
  const { isAdmin } = useAuth();
  const [tasks, setTasks] = useState({ content: [], page: 0, totalPages: 0 });
  const [projects, setProjects] = useState([]);
  const [developers, setDevelopers] = useState([]);
  const [taskForm, setTaskForm] = useState(initialTaskForm);
  const [filters, setFilters] = useState({ search: '', status: '', projectId: '' });
  const [selectedTask, setSelectedTask] = useState(null);
  const [comment, setComment] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [editingId, setEditingId] = useState(null);

  const loadTasks = async (page = 0) => {
    setLoading(true);
    try {
      setTasks(await taskService.getTasks({ ...filters, page, size: 6 }));
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const bootstrap = async () => {
      setLoading(true);
      try {
        const [projectResponse, developerResponse, taskResponse] = await Promise.all([
          projectService.getProjects({ page: 0, size: 50 }),
          userService.getDevelopers(),
          taskService.getTasks({ page: 0, size: 6 }),
        ]);
        setProjects(projectResponse.content);
        setDevelopers(developerResponse);
        setTasks(taskResponse);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    bootstrap();
  }, []);

  const handleTaskSubmit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      const payload = {
        ...taskForm,
        projectId: Number(taskForm.projectId),
        assignedUserId: taskForm.assignedUserId ? Number(taskForm.assignedUserId) : null,
      };

      if (editingId) {
        await taskService.updateTask(editingId, payload);
      } else {
        await taskService.createTask(payload);
      }

      setTaskForm(initialTaskForm);
      setEditingId(null);
      await loadTasks(tasks.page);
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Tasks"
        title="Track work across projects"
        description="Use filters, inline updates, and collaboration notes to keep delivery flowing."
        action={<button className="btn-secondary" onClick={() => loadTasks(0)}>Refresh</button>}
      />

      <div className="grid gap-4 xl:grid-cols-[1.25fr_0.75fr]">
        <div className="glass-panel p-6">
          <div className="grid gap-3 md:grid-cols-4">
            <input className="input md:col-span-2" value={filters.search} onChange={(e) => setFilters({ ...filters, search: e.target.value })} placeholder="Search tasks" />
            <select className="input" value={filters.status} onChange={(e) => setFilters({ ...filters, status: e.target.value })}>
              <option value="">All statuses</option>
              <option value="TODO">Todo</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="COMPLETED">Completed</option>
            </select>
            <button className="btn-primary" onClick={() => loadTasks(0)}>Apply</button>
          </div>

          <div className="mt-6 space-y-4">
            {loading ? (
              <Loader label="Loading tasks..." />
            ) : tasks.content.length === 0 ? (
              <EmptyState title="No tasks found" description="Try broadening your filters or create a new task." />
            ) : (
              tasks.content.map((task) => (
                <div key={task.id} className="rounded-3xl border border-slate-100 bg-white p-5">
                  <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
                    <div className="space-y-3">
                      <div className="flex flex-wrap items-center gap-2">
                        <h3 className="text-lg font-bold text-ink">{task.title}</h3>
                        <span className={`rounded-full px-3 py-1 text-xs font-semibold ${getPriorityBadge(task.priority)}`}>{task.priority}</span>
                        <span className={`rounded-full px-3 py-1 text-xs font-semibold ${getStatusBadge(task.status)}`}>{task.status.replace('_', ' ')}</span>
                      </div>
                      <p className="text-sm text-slate-500">{task.description}</p>
                      <div className="grid gap-2 text-sm text-slate-500 md:grid-cols-2">
                        <p>Project: {task.projectName}</p>
                        <p>Assignee: {task.assignedUser?.fullName || 'Unassigned'}</p>
                        <p>Deadline: {formatDate(task.deadline)}</p>
                        <p>Comments: {task.comments.length}</p>
                      </div>
                    </div>

                    <div className="flex flex-wrap gap-2">
                      <button className="btn-secondary" onClick={() => setSelectedTask(task)}>Details</button>
                      {isAdmin && (
                        <>
                          <button className="btn-secondary" onClick={() => {
                            setEditingId(task.id);
                            setTaskForm({
                              title: task.title,
                              description: task.description || '',
                              priority: task.priority,
                              status: task.status,
                              deadline: task.deadline || '',
                              projectId: String(task.projectId),
                              assignedUserId: task.assignedUser?.id ? String(task.assignedUser.id) : '',
                            });
                          }}>
                            Edit
                          </button>
                          <button className="btn-secondary" onClick={async () => {
                            await taskService.deleteTask(task.id);
                            await loadTasks(tasks.page);
                          }}>
                            Delete
                          </button>
                        </>
                      )}
                      {!isAdmin && (
                        <select
                          className="input !w-auto !py-2"
                          value={task.status}
                          onChange={async (event) => {
                            await taskService.updateStatus(task.id, { status: event.target.value });
                            await loadTasks(tasks.page);
                          }}
                        >
                          <option value="TODO">Todo</option>
                          <option value="IN_PROGRESS">In Progress</option>
                          <option value="COMPLETED">Completed</option>
                        </select>
                      )}
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>

          <Pagination page={tasks.page} totalPages={tasks.totalPages} onChange={loadTasks} />
        </div>

        <div className="space-y-4">
          <div className="glass-panel p-6">
            <p className="text-sm font-semibold text-slate-500">Task workspace</p>
            <h3 className="mt-1 text-xl font-bold text-ink">{editingId ? 'Edit task' : 'Create task'}</h3>
            {!isAdmin ? (
              <p className="mt-4 text-sm text-slate-500">Admins can create tasks. Developers can update task status and contribute comments.</p>
            ) : (
              <form className="mt-6 space-y-4" onSubmit={handleTaskSubmit}>
                <input className="input" value={taskForm.title} onChange={(e) => setTaskForm({ ...taskForm, title: e.target.value })} placeholder="Task title" />
                <textarea className="input min-h-24" value={taskForm.description} onChange={(e) => setTaskForm({ ...taskForm, description: e.target.value })} placeholder="Task description" />
                <select className="input" value={taskForm.priority} onChange={(e) => setTaskForm({ ...taskForm, priority: e.target.value })}>
                  <option value="LOW">Low</option>
                  <option value="MEDIUM">Medium</option>
                  <option value="HIGH">High</option>
                </select>
                <select className="input" value={taskForm.status} onChange={(e) => setTaskForm({ ...taskForm, status: e.target.value })}>
                  <option value="TODO">Todo</option>
                  <option value="IN_PROGRESS">In Progress</option>
                  <option value="COMPLETED">Completed</option>
                </select>
                <input className="input" type="date" value={taskForm.deadline} onChange={(e) => setTaskForm({ ...taskForm, deadline: e.target.value })} />
                <select className="input" value={taskForm.projectId} onChange={(e) => setTaskForm({ ...taskForm, projectId: e.target.value })}>
                  <option value="">Select project</option>
                  {projects.map((project) => <option key={project.id} value={project.id}>{project.name}</option>)}
                </select>
                <select className="input" value={taskForm.assignedUserId} onChange={(e) => setTaskForm({ ...taskForm, assignedUserId: e.target.value })}>
                  <option value="">Assign developer</option>
                  {developers.map((developer) => <option key={developer.id} value={developer.id}>{developer.fullName}</option>)}
                </select>
                {error && <p className="text-sm text-rose-600">{error}</p>}
                <button className="btn-primary w-full" disabled={saving}>
                  {saving ? 'Saving...' : editingId ? 'Update task' : 'Create task'}
                </button>
              </form>
            )}
          </div>

          <div className="glass-panel p-6">
            <p className="text-sm font-semibold text-slate-500">Task details</p>
            {!selectedTask ? (
              <p className="mt-4 text-sm text-slate-500">Select a task to review comments and attachments.</p>
            ) : (
              <div className="mt-4 space-y-4">
                <div>
                  <h4 className="text-lg font-bold text-ink">{selectedTask.title}</h4>
                  <p className="mt-2 text-sm text-slate-500">{selectedTask.description}</p>
                </div>

                <div>
                  <p className="text-sm font-semibold text-slate-500">Comments</p>
                  <div className="mt-3 space-y-3">
                    {selectedTask.comments.map((item) => (
                      <div key={item.id} className="rounded-2xl bg-slate-50 p-3">
                        <p className="text-sm font-semibold text-ink">{item.authorName}</p>
                        <p className="mt-1 text-sm text-slate-500">{item.content}</p>
                      </div>
                    ))}
                  </div>
                  <div className="mt-3 space-y-3">
                    <textarea className="input min-h-24" value={comment} onChange={(e) => setComment(e.target.value)} placeholder="Add a comment" />
                    <button className="btn-primary w-full" onClick={async () => {
                      await taskService.addComment(selectedTask.id, { content: comment });
                      setComment('');
                      const refreshed = await taskService.getTasks({ page: tasks.page, size: 6 });
                      setTasks(refreshed);
                      setSelectedTask(refreshed.content.find((task) => task.id === selectedTask.id) || null);
                    }}>
                      Post comment
                    </button>
                  </div>
                </div>

                <div>
                  <p className="text-sm font-semibold text-slate-500">Attachments</p>
                  <div className="mt-3 space-y-2">
                    {selectedTask.attachments.map((attachment) => (
                      <div key={attachment.id} className="rounded-2xl bg-slate-50 p-3 text-sm text-slate-600">
                        {attachment.fileName}
                      </div>
                    ))}
                  </div>
                  <input
                    className="input mt-3"
                    type="file"
                    onChange={async (event) => {
                      const file = event.target.files?.[0];
                      if (!file) return;
                      const response = await taskService.uploadAttachment(selectedTask.id, file);
                      setSelectedTask(response);
                      await loadTasks(tasks.page);
                    }}
                  />
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default TasksPage;
