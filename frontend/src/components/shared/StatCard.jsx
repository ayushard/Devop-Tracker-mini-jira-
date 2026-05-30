function StatCard({ label, value, tone, helper }) {
  return (
    <div className={`glass-panel animate-rise p-6 ${tone}`}>
      <p className="text-sm text-slate-500">{label}</p>
      <div className="mt-4 flex items-end justify-between gap-4">
        <h3 className="text-3xl font-extrabold text-ink">{value}</h3>
        <span className="rounded-full bg-white/80 px-3 py-1 text-xs font-semibold text-slate-600">{helper}</span>
      </div>
    </div>
  );
}

export default StatCard;
