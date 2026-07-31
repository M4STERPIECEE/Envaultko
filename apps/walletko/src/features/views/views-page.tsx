import {
  useMutation,
  useQueryClient,
  useSuspenseQuery,
} from "@tanstack/react-query";
import { PlusIcon } from "lucide-react";
import { useState } from "react";
import { tagsQuery } from "src/features/tags/queries";
import { CreateViewDialog } from "src/features/views/components/create-view-dialog";
import { EditViewDialog } from "src/features/views/components/edit-view-dialog";
import { ViewCard } from "src/features/views/components/view-card";
import { viewKeys, viewsQuery } from "src/features/views/queries";
import { type SavedViewDTO, viewsApi } from "src/shared/api/views";
import { PageContent, PageHeader } from "src/shared/layout/page";
import { ConfirmDeleteDialog } from "src/shared/ui/confirm-delete-dialog";
import { EmptyState } from "src/shared/ui/empty-state";
import { PageActions } from "src/shared/ui/page-actions";

export function ViewsPage() {
  const [createOpen, setCreateOpen] = useState(false);
  const [editingView, setEditingView] = useState<SavedViewDTO | null>(null);
  const [deletingView, setDeletingView] = useState<SavedViewDTO | null>(null);

  const qc = useQueryClient();
  const { data: views } = useSuspenseQuery(viewsQuery);
  const { data: tags } = useSuspenseQuery(tagsQuery);

  const tagNames: Record<string, string> = Object.fromEntries(
    tags.map((t) => [t.id, t.name]),
  );

  const deleteMutation = useMutation({
    mutationFn: (args: { data: { id: string } }) =>
      viewsApi.delete(args.data.id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: viewKeys.list() });
      setDeletingView(null);
    },
  });

  return (
    <PageContent className="space-y-8 pb-12">
      <PageHeader
        eyebrow="Saved Filters & Insights"
        title="Custom Views"
        action={
          <PageActions
            primary={{
              key: "new-view",
              label: "New View",
              icon: <PlusIcon className="size-4" />,
              onClick: () => setCreateOpen(true),
            }}
          />
        }
      />

      {views.length === 0 && (
        <EmptyState>
          No views yet. Create your first view above to save custom filters.
        </EmptyState>
      )}

      {views.length > 0 && (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {views.map((view) => (
            <ViewCard
              key={view.id}
              view={view}
              tagNames={tagNames}
              onEdit={setEditingView}
              onDelete={setDeletingView}
            />
          ))}
        </div>
      )}

      <CreateViewDialog
        open={createOpen}
        onClose={() => setCreateOpen(false)}
      />

      {editingView && (
        <EditViewDialog
          open={!!editingView}
          onClose={() => setEditingView(null)}
          view={editingView}
        />
      )}

      <ConfirmDeleteDialog
        open={!!deletingView}
        onClose={() => setDeletingView(null)}
        onConfirm={() =>
          deletingView &&
          deleteMutation.mutate({ data: { id: deletingView.id } })
        }
        title="Delete View"
        items={deletingView?.name ?? ""}
        isLoading={deleteMutation.isPending}
        error={deleteMutation.isError ? "Failed to delete view." : undefined}
      />
    </PageContent>
  );
}
