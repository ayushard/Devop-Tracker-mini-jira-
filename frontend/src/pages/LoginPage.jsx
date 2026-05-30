import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

function LoginPage() {
  const navigate = useNavigate();
  const { login, loading } = useAuth();
  const [error, setError] = useState('');
  const [form, setForm] = useState({ email: 'admin@devops.local', password: 'Admin@123' });

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await login(form);
      navigate('/');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center p-6">
      <div className="grid w-full max-w-5xl overflow-hidden rounded-[32px] bg-white shadow-soft lg:grid-cols-[1.15fr_0.85fr]">
        <div className="bg-ink p-10 text-white">
          <p className="text-xs uppercase tracking-[0.35em] text-teal-200">DevOps Project Management Dashboard</p>
          <h1 className="mt-6 text-5xl font-extrabold leading-tight">Operate delivery with clarity, momentum, and accountability.</h1>
          <p className="mt-6 max-w-lg text-base text-slate-300">
            Seeded accounts are ready so you can explore the app immediately after setup.
          </p>
          <div className="mt-8 rounded-3xl border border-white/10 bg-white/5 p-5">
            <p className="text-sm font-semibold">Demo credentials</p>
            <p className="mt-3 text-sm text-slate-300">Admin: admin@devops.local / Admin@123</p>
            <p className="text-sm text-slate-300">Developer: developer1@devops.local / Dev@123</p>
          </div>
        </div>

        <div className="p-8 md:p-10">
          <p className="text-sm font-semibold uppercase tracking-[0.25em] text-accent">Welcome back</p>
          <h2 className="mt-3 text-3xl font-extrabold text-ink">Sign in to your workspace</h2>

          <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
            <input className="input" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="Email" />
            <input className="input" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="Password" />
            {error && <p className="text-sm text-rose-600">{error}</p>}
            <button type="submit" className="btn-primary w-full" disabled={loading}>
              {loading ? 'Signing in...' : 'Login'}
            </button>
          </form>

          <p className="mt-6 text-sm text-slate-500">
            Need an account? <Link to="/register" className="font-semibold text-ink">Create one</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
