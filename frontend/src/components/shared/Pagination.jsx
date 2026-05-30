function Pagination({ page, totalPages, onChange }) {
  if (totalPages <= 1) return null;

  return (
    <div className="flex items-center justify-between pt-4">
      <button className="btn-secondary" onClick={() => onChange(page - 1)} disabled={page <= 0}>
        Previous
      </button>
      <p className="text-sm text-slate-500">
        Page {page + 1} of {totalPages}
      </p>
      <button className="btn-secondary" onClick={() => onChange(page + 1)} disabled={page + 1 >= totalPages}>
        Next
      </button>
    </div>
  );
}

export default Pagination;
