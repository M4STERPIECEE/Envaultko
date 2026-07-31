import { useQuery } from "@tanstack/react-query";
import { Link } from "@tanstack/react-router";
import { ChevronRightIcon, PencilIcon, Trash2Icon } from "lucide-react";
import { viewStatsQuery } from "src/features/views/queries";
import type { SavedViewDTO } from "src/shared/api/views";
import { cn } from "src/shared/lib/utils";
import { Badge } from "src/shared/ui/badge";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "src/shared/ui/card";
import {
  DropdownMenuItem,
  DropdownMenuSeparator,
} from "src/shared/ui/dropdown-menu";
import { Money } from "src/shared/ui/money";
import { RowActions } from "src/shared/ui/row-actions";

function ViewStatsPreview({ viewId }: { viewId: string }) {
  const { data: stats } = useQuery(viewStatsQuery(viewId));
  if (!stats) return <span className="text-muted-foreground text-sm">—</span>;
  const isPositive = stats.balance >= 0;
  return (
    <span
      className={cn(
        "font-bold text-sm tabular-nums",
        isPositive
          ? "text-income dark:text-emerald-400"
          : "text-destructive dark:text-rose-400",
      )}
    >
      <Money value={stats.balance} />
    </span>
  );
}

export type ViewCardProps = {
  view: SavedViewDTO;
  tagNames: Record<string, string>;
  onEdit: (view: SavedViewDTO) => void;
  onDelete: (view: SavedViewDTO) => void;
};

export function ViewCard({ view, tagNames, onEdit, onDelete }: ViewCardProps) {
  return (
    <Card className="group/view relative overflow-hidden transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-primary/5 hover:border-primary/30 flex flex-col">
      <Link
        to="/views/$viewId"
        params={{ viewId: view.id }}
        aria-label={`Open ${view.name}`}
        className="absolute inset-0 z-0 rounded-xl"
      />

      <CardHeader className="pb-3">
        <div className="flex items-start justify-between gap-2">
          <div className="space-y-1 min-w-0 flex-1">
            <CardTitle className="text-base font-bold text-foreground/90 group-hover/view:text-primary transition-colors truncate">
              {view.name}
            </CardTitle>
            {view.description && (
              <CardDescription className="line-clamp-2 text-xs">
                {view.description}
              </CardDescription>
            )}
          </div>
          <CardAction className="relative z-10 shrink-0">
            <RowActions label={`Options for ${view.name}`}>
              <DropdownMenuItem
                className="cursor-pointer gap-2"
                onClick={() => onEdit(view)}
              >
                <PencilIcon className="size-4" />
                Edit
              </DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                className="cursor-pointer gap-2 text-destructive focus:text-destructive"
                variant="destructive"
                onClick={() => onDelete(view)}
              >
                <Trash2Icon className="size-4" />
                Delete
              </DropdownMenuItem>
            </RowActions>
          </CardAction>
        </div>
      </CardHeader>

      <CardContent className="flex-1 pb-4">
        <div className="flex flex-wrap gap-1.5">
          {view.nameFilter && (
            <Badge variant="secondary" className="text-xs bg-muted/70">
              name: {view.nameFilter}
            </Badge>
          )}
          {view.tagIds.map((id) => (
            <Badge key={id} variant="secondary" className="text-xs bg-muted/70">
              {tagNames[id] ?? id}
            </Badge>
          ))}
        </div>
      </CardContent>

      <CardFooter className="pt-3 border-t bg-muted/20 justify-between items-center">
        <div className="flex items-center gap-1.5 text-xs font-medium text-muted-foreground uppercase tracking-wider">
          Net <ViewStatsPreview viewId={view.id} />
        </div>
        <ChevronRightIcon className="size-4 text-muted-foreground transition-transform duration-200 group-hover/view:translate-x-1 group-hover/view:text-primary" />
      </CardFooter>
    </Card>
  );
}
