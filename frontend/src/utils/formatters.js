export function formatDate(value) {
  if (!value) return 'No deadline';
  return new Date(value).toLocaleDateString();
}

export function getStatusBadge(status) {
  return {
    TODO: 'bg-slate-100 text-slate-700',
    IN_PROGRESS: 'bg-amber-100 text-amber-700',
    COMPLETED: 'bg-emerald-100 text-emerald-700',
  }[status] || 'bg-slate-100 text-slate-700';
}

export function getPriorityBadge(priority) {
  return {
    LOW: 'bg-sky-100 text-sky-700',
    MEDIUM: 'bg-violet-100 text-violet-700',
    HIGH: 'bg-rose-100 text-rose-700',
  }[priority] || 'bg-slate-100 text-slate-700';
}
