import { useEffect, useState } from 'react';
import ProgressChart from '../components/charts/ProgressChart';
import EmptyState from '../components/shared/EmptyState';
import Loader from '../components/shared/Loader';
import PageHeader from '../components/shared/PageHeader';
import StatCard from '../components/shared/StatCard';
import { dashboardService } from '../services/dashboardService';

function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadStats = async () => {
      setLoading(true);
      try {
        setStats(await dashboardService.getStats());
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    loadStats();
  }, []);

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Overview"
        title="Delivery dashboard"
        description="Track project health, task throughput, and recent team movement from one command center."
      />

      {loading ? (
        <Loader label="Loading dashboard insights..." />
      ) : error ? (
        <EmptyState title="Dashboard unavailable" description={error} />
      ) : (
        <>
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
            <StatCard label="Total projects" value={stats.totalProjects} helper="Active portfolio" />
            <StatCard label="Total tasks" value={stats.totalTasks} helper="Across all teams" />
            <StatCard label="Completed tasks" value={stats.completedTasks} helper="Closed work" />
            <StatCard label="Pending tasks" value={stats.pendingTasks} helper="Needs attention" />
          </div>

          <div className="grid gap-4 xl:grid-cols-[1.2fr_0.8fr]">
            <ProgressChart data={stats.projectProgress} />
            <div className="glass-panel p-6">
              <p className="text-sm font-semibold text-slate-500">Recent Activity</p>
              <h3 className="mt-1 text-xl font-bold text-ink">Latest actions in the workspace</h3>
              <div className="mt-6 space-y-4">
                {stats.recentActivities.length === 0 ? (
                  <p className="text-sm text-slate-500">No recent activity yet.</p>
                ) : (
                  stats.recentActivities.map((item) => (
                    <div key={item.id} className="rounded-2xl border border-slate-100 bg-slate-50 p-4">
                      <p className="text-sm font-semibold text-ink">
                        {item.actorName} {item.action} {item.targetType.toLowerCase()} <span className="text-accent">{item.targetName}</span>
                      </p>
                      <p className="mt-1 text-xs text-slate-500">{item.createdAt}</p>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default DashboardPage;
