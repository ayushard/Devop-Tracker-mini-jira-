function Loader({ label = 'Loading...' }) {
  return (
    <div className="flex items-center justify-center rounded-3xl border border-dashed border-slate-300 bg-white/60 px-6 py-10 text-sm text-slate-500">
      {label}
    </div>
  );
}

export default Loader;
