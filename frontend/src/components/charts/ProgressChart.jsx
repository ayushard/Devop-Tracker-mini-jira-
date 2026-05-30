function ProgressChart({ data }) {
  const maxTotal = Math.max(...data.map((item) => item.totalTasks || 0), 1);

  return (
    <div className="glass-panel p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-semibold text-slate-500">Project Progress</p>
          <h3 className="mt-1 text-xl font-bold text-ink">Delivery completion snapshot</h3>
        </div>
      </div>

      <div className="mt-6 space-y-5">
        {data.map((item) => {
          const completedWidth = `${((item.completedTasks || 0) / maxTotal) * 100}%`;
          const totalWidth = `${((item.totalTasks || 0) / maxTotal) * 100}%`;

          return (
            <div key={item.name}>
              <div className="mb-2 flex items-center justify-between text-sm">
                <span className="font-semibold text-ink">{item.name}</span>
                <span className="text-slate-500">
                  {item.completedTasks}/{item.totalTasks} complete
                </span>
              </div>
              <div className="relative h-3 rounded-full bg-slate-100">
                <div className="absolute inset-y-0 left-0 rounded-full bg-slate-300" style={{ width: totalWidth }} />
                <div className="absolute inset-y-0 left-0 rounded-full bg-gradient-to-r from-accent to-teal-400" style={{ width: completedWidth }} />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default ProgressChart;
