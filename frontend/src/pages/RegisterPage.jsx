import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

function RegisterPage() {
  const navigate = useNavigate();
  const { register, loading } = useAuth();
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    roles: ['ROLE_DEVELOPER'],
  });

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await register(form);
      navigate('/');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center p-6">
      <div className="w-full max-w-xl rounded-[32px] bg-white p-8 shadow-soft md:p-10">
        <p className="text-sm font-semibold uppercase tracking-[0.25em] text-accent">Create account</p>
        <h1 className="mt-3 text-3xl font-extrabold text-ink">Start collaborating with your team</h1>

        <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
          <input className="input" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} placeholder="Full name" />
          <input className="input" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="Email" />
          <input className="input" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="Password" />
          <select className="input" value={form.roles[0]} onChange={(e) => setForm({ ...form, roles: [e.target.value] })}>
            <option value="ROLE_DEVELOPER">Developer</option>
            <option value="ROLE_ADMIN">Admin</option>
          </select>
          {error && <p className="text-sm text-rose-600">{error}</p>}
          <button type="submit" className="btn-primary w-full" disabled={loading}>
            {loading ? 'Creating account...' : 'Register'}
          </button>
        </form>

        <p className="mt-6 text-sm text-slate-500">
          Already have an account? <Link to="/login" className="font-semibold text-ink">Sign in</Link>
        </p>
      </div>
    </div>
  );
}

export default RegisterPage;
