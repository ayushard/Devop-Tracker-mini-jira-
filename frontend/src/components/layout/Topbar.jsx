import { Bell, LogOut, Search } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

function Topbar() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [query, setQuery] = useState('');

  return (
    <header className="flex flex-col gap-4 border-b border-slate-200/80 bg-white/70 p-4 md:flex-row md:items-center md:justify-between md:px-6">
      <div className="flex items-center gap-3 rounded-2xl border border-slate-200 bg-white px-4 py-3 md:w-[360px]">
        <Search size={18} className="text-slate-400" />
        <input
          value={query}
          onChange={(event) => setQuery(event.target.value)}
          className="w-full border-none bg-transparent text-sm outline-none"
          placeholder="Global search UI placeholder"
        />
      </div>

      <div className="flex items-center gap-3">
        <button className="btn-secondary !rounded-full !p-3">
          <Bell size={18} />
        </button>
        <button
          className="btn-primary gap-2"
          onClick={() => {
            logout();
            navigate('/login');
          }}
        >
          <LogOut size={16} />
          Logout
        </button>
      </div>
    </header>
  );
}

export default Topbar;
