import {
  keepPreviousData,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import { PencilIcon, PlusIcon, Tag, Trash2Icon } from "lucide-react";
import { useState } from "react";
import { AddTagDialog } from "src/features/tags/components/add-tag-dialog";
import { EditTagDialog } from "src/features/tags/components/edit-tag-dialog";
import { TagListSkeleton } from "src/features/tags/components/tag-list-skeleton";
import {
  TAGS_PAGE_SIZE,
  tagKeys,
  tagsPagedQuery,
} from "src/features/tags/queries";
import type { TagDTO } from "src/shared/api/tags";
import { tagsApi } from "src/shared/api/tags";
import { PageContent, PageHeader } from "src/shared/layout/page";
import { ConfirmDeleteDialog } from "src/shared/ui/confirm-delete-dialog";
import { DataList, DataListHead, DataListRow } from "src/shared/ui/data-list";
import { DropdownMenuItem } from "src/shared/ui/dropdown-menu";
import { EmptyState } from "src/shared/ui/empty-state";
import { ErrorState } from "src/shared/ui/error-state";
import { PageActions } from "src/shared/ui/page-actions";
import { Pagination } from "src/shared/ui/pagination";
import { RowActions } from "src/shared/ui/row-actions";

type TagListItem = TagDTO;

export function TagsPage() {
  const [page, setPage] = useState(1);
  const [addOpen, setAddOpen] = useState(false);
  const [editingTag, setEditingTag] = useState<TagListItem | null>(null);
  const [deletingTag, setDeletingTag] = useState<TagListItem | null>(null);

  const qc = useQueryClient();
  const { data, isLoading, isError, refetch } = useQuery({
    ...tagsPagedQuery(page, TAGS_PAGE_SIZE),
    placeholderData: keepPreviousData,
  });

  const deleteTagMutation = useMutation({
    mutationFn: (args: { data: { id: string } }) =>
      tagsApi.delete(args.data.id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: tagKeys.all });
      setDeletingTag(null);
    },
  });

  const items = data?.items ?? [];
  const total = data?.total ?? 0;
  const totalPages = Math.ceil(total / TAGS_PAGE_SIZE);

  return (
    <PageContent className="space-y-8 pb-12">
      <PageHeader
        eyebrow="Organization & Categorization"
        title="Tags"
        action={
          <PageActions
            primary={{
              key: "add-tag",
              label: "Add Tag",
              icon: <PlusIcon className="size-4" />,
              onClick: () => setAddOpen(true),
            }}
          />
        }
      />

      {isLoading && <TagListSkeleton />}

      {isError && (
        <ErrorState
          description="We couldn't load your tags."
          onRetry={() => refetch()}
        />
      )}

      {!isLoading && !isError && items.length === 0 && (
        <EmptyState>No tags yet. Add your first tag above.</EmptyState>
      )}

      {!isLoading && !isError && items.length > 0 && (
        <DataList
          header={
            <>
              <DataListHead className="flex-1">Tag Name</DataListHead>
              <div className="size-8 shrink-0" />
            </>
          }
        >
          {items.map((tag) => (
            <DataListRow
              key={tag.id}
              className="group transition-all duration-200 hover:bg-muted/40 p-3.5 rounded-xl border border-transparent hover:border-border/60"
            >
              <div className="flex items-center gap-2.5 min-w-0 flex-1">
                <div className="flex size-7 shrink-0 items-center justify-center rounded-lg bg-primary/10 text-primary">
                  <Tag className="size-3.5" />
                </div>
                <span className="truncate text-sm font-semibold text-foreground/90 group-hover:text-foreground">
                  {tag.name}
                </span>
              </div>

              <RowActions label={`Actions for ${tag.name}`}>
                <DropdownMenuItem
                  className="cursor-pointer gap-2"
                  onClick={() => setEditingTag(tag)}
                >
                  <PencilIcon className="size-4" />
                  Edit
                </DropdownMenuItem>
                <DropdownMenuItem
                  className="cursor-pointer gap-2 text-destructive focus:text-destructive"
                  variant="destructive"
                  onClick={() => setDeletingTag(tag)}
                >
                  <Trash2Icon className="size-4" />
                  Delete
                </DropdownMenuItem>
              </RowActions>
            </DataListRow>
          ))}
        </DataList>
      )}

      <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />

      <AddTagDialog open={addOpen} onClose={() => setAddOpen(false)} />

      {editingTag && (
        <EditTagDialog
          open={!!editingTag}
          onClose={() => setEditingTag(null)}
          tag={editingTag}
        />
      )}

      {deletingTag && (
        <ConfirmDeleteDialog
          open={!!deletingTag}
          onClose={() => setDeletingTag(null)}
          onConfirm={() =>
            deleteTagMutation.mutate({ data: { id: deletingTag.id } })
          }
          title="Delete tag"
          items={[deletingTag.name]}
          isLoading={deleteTagMutation.isPending}
          error={
            deleteTagMutation.isError
              ? "Something went wrong. Please try again."
              : undefined
          }
        />
      )}
    </PageContent>
  );
}
