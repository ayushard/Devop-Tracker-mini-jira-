import { useAuth } from '../hooks/useAuth';

function ProfilePage() {
  const { user } = useAuth();

  return (
    <div className="space-y-6">
      <div className="glass-panel p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.25em] text-accent">Profile</p>
        <h1 className="mt-3 text-3xl font-extrabold text-ink">Your workspace identity</h1>

        <div className="mt-8 grid gap-4 md:grid-cols-2">
          <div className="rounded-3xl bg-slate-50 p-5">
            <p className="text-sm text-slate-500">Full name</p>
            <p className="mt-2 text-lg font-bold text-ink">{user?.fullName}</p>
          </div>
          <div className="rounded-3xl bg-slate-50 p-5">
            <p className="text-sm text-slate-500">Email</p>
            <p className="mt-2 text-lg font-bold text-ink">{user?.email}</p>
          </div>
          <div className="rounded-3xl bg-slate-50 p-5">
            <p className="text-sm text-slate-500">Roles</p>
            <p className="mt-2 text-lg font-bold text-ink">{user?.roles?.join(', ').replaceAll('ROLE_', '')}</p>
          </div>
          <div className="rounded-3xl bg-slate-50 p-5">
            <p className="text-sm text-slate-500">Joined</p>
            <p className="mt-2 text-lg font-bold text-ink">{new Date(user?.createdAt).toLocaleString()}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfilePage;
