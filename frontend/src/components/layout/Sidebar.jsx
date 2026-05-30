import { FolderKanban, LayoutDashboard, ListTodo, UserCircle2 } from 'lucide-react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const links = [
  { to: '/', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/projects', label: 'Projects', icon: FolderKanban },
  { to: '/tasks', label: 'Tasks', icon: ListTodo },
  { to: '/profile', label: 'Profile', icon: UserCircle2 },
];

function Sidebar() {
  const { user } = useAuth();

  return (
    <aside className="glass-panel flex flex-col justify-between p-5">
      <div>
        <div className="rounded-3xl bg-ink p-5 text-white">
          <p className="text-xs uppercase tracking-[0.35em] text-teal-200">DevOps PM</p>
          <h2 className="mt-3 text-2xl font-extrabold">Delivery cockpit for modern teams.</h2>
          <p className="mt-3 text-sm text-slate-300">Plan releases, unblock developers, and keep workflow visible.</p>
        </div>

        <nav className="mt-6 space-y-2">
          {links.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              end={to === '/'}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-semibold transition ${
                  isActive ? 'bg-slate-900 text-white' : 'text-slate-600 hover:bg-slate-100'
                }`
              }
            >
              <Icon size={18} />
              {label}
            </NavLink>
          ))}
        </nav>
      </div>

      <div className="rounded-3xl border border-slate-200 bg-slate-50 p-4">
        <p className="text-xs font-semibold uppercase tracking-[0.25em] text-slate-400">Signed In</p>
        <p className="mt-2 text-base font-bold text-ink">{user?.fullName}</p>
        <p className="text-sm text-slate-500">{user?.roles?.join(', ').replaceAll('ROLE_', '')}</p>
      </div>
    </aside>
  );
}

export default Sidebar;
