import { ArchiveIcon, PencilIcon } from "lucide-react";
import { useFormatDate } from "src/shared/hooks/use-format-date";
import { DataListRow } from "src/shared/ui/data-list";
import { DropdownMenuItem } from "src/shared/ui/dropdown-menu";
import { Money } from "src/shared/ui/money";
import { RowActions } from "src/shared/ui/row-actions";

export type PotListItem = {
  id: string;
  name: string;
  percentage: number;
  balance: number;
  color: string;
  isDefault: boolean;
  createdAt: Date | string;
};

type PotRowProps = {
  pot: PotListItem;
  onEdit: (pot: PotListItem) => void;
  onArchive: (pot: PotListItem) => void;
};

export function PotRow({ pot, onEdit, onArchive }: PotRowProps) {
  const formatDate = useFormatDate();

  return (
    <DataListRow className="group transition-all duration-200 hover:bg-muted/40 p-4 rounded-xl border border-transparent hover:border-border/60">
      <div className="flex items-center gap-3 min-w-0 flex-1">
        <span
          className="size-3 shrink-0 rounded-full shadow-sm transition-transform duration-200 group-hover:scale-110"
          style={{ backgroundColor: pot.color }}
        />
        <div className="min-w-0 flex-1 space-y-0.5">
          <div className="flex items-center gap-2">
            <p className="truncate text-sm font-semibold text-foreground/90 group-hover:text-foreground">
              {pot.name}
            </p>
            {pot.isDefault && (
              <span className="text-[10px] uppercase font-bold tracking-wider px-1.5 py-0.2 rounded bg-primary/10 text-primary">
                Default
              </span>
            )}
          </div>
          <p className="text-xs text-muted-foreground tabular-nums">
            {formatDate(pot.createdAt)}
          </p>
        </div>
      </div>

      <div className="w-28 shrink-0 space-y-1 text-right">
        <span className="inline-flex items-center rounded-full bg-muted/60 px-2 py-0.5 text-xs font-semibold text-muted-foreground tabular-nums">
          {pot.percentage}%
        </span>
      </div>

      <Money
        value={pot.balance}
        className="w-28 text-right text-sm font-bold tracking-tight tabular-nums"
      />

      <RowActions label={`Actions for ${pot.name}`}>
        <DropdownMenuItem
          className="cursor-pointer gap-2"
          onClick={() => onEdit(pot)}
        >
          <PencilIcon className="size-4" />
          Edit
        </DropdownMenuItem>
        {!pot.isDefault && (
          <DropdownMenuItem
            className="cursor-pointer gap-2 text-destructive focus:text-destructive"
            variant="destructive"
            onClick={() => onArchive(pot)}
          >
            <ArchiveIcon className="size-4" />
            Archive
          </DropdownMenuItem>
        )}
      </RowActions>
    </DataListRow>
  );
}
