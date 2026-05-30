import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import Topbar from './Topbar';

function Layout() {
  return (
    <div className="min-h-screen bg-transparent p-4 lg:p-6">
      <div className="mx-auto grid min-h-[calc(100vh-2rem)] max-w-7xl gap-4 lg:grid-cols-[280px_minmax(0,1fr)]">
        <Sidebar />
        <div className="glass-panel overflow-hidden">
          <Topbar />
          <main className="space-y-6 p-4 md:p-6">
            <Outlet />
          </main>
        </div>
      </div>
    </div>
  );
}

export default Layout;
