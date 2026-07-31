import {
  ArrowDownLeft,
  ArrowLeftRight,
  ArrowUpRight,
  Pencil,
  Trash2,
} from "lucide-react";
import type { TransactionDTO } from "src/shared/api/transactions";
import { useFormatDate } from "src/shared/hooks/use-format-date";
import { cn } from "src/shared/lib/utils";
import { Badge } from "src/shared/ui/badge";
import { DataListRow } from "src/shared/ui/data-list";
import { DropdownMenuItem } from "src/shared/ui/dropdown-menu";
import { HighlightMatch } from "src/shared/ui/highlight-match";
import { Money } from "src/shared/ui/money";
import { RowActions } from "src/shared/ui/row-actions";

export function TransactionRow({
  tx,
  nameQuery,
  onEdit,
  onDelete,
}: {
  tx: TransactionDTO;
  nameQuery: string;
  onEdit?: (id: string) => void;
  onDelete?: (id: string) => void;
}) {
  const formatDate = useFormatDate();

  return (
    <DataListRow className="group transition-all duration-200 hover:bg-muted/40 p-3.5 rounded-xl border border-transparent hover:border-border/60">
      <div
        className={cn(
          "flex size-9 shrink-0 items-center justify-center rounded-xl transition-transform duration-200 group-hover:scale-105",
          tx.type === "income" &&
            "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400",
          tx.type === "transfer" &&
            "bg-indigo-500/10 text-indigo-600 dark:text-indigo-400",
          tx.type === "expense" &&
            "bg-rose-500/10 text-rose-600 dark:text-rose-400",
          tx.type === "canceled_income" && "bg-muted text-muted-foreground",
          tx.type === "income_cancellation" &&
            "bg-indigo-500/10 text-indigo-500",
          tx.type === "canceled_expense" && "bg-muted text-muted-foreground",
          tx.type === "expense_cancellation" &&
            "bg-indigo-500/10 text-indigo-500",
        )}
      >
        {tx.type === "income" && <ArrowDownLeft className="size-4" />}
        {tx.type === "transfer" && <ArrowLeftRight className="size-4" />}
        {tx.type === "expense" && <ArrowUpRight className="size-4" />}
        {tx.type === "canceled_income" && (
          <ArrowDownLeft className="size-4 text-muted-foreground" />
        )}
        {tx.type === "income_cancellation" && (
          <ArrowLeftRight className="size-4" />
        )}
        {tx.type === "canceled_expense" && (
          <ArrowUpRight className="size-4 text-muted-foreground" />
        )}
        {tx.type === "expense_cancellation" && (
          <ArrowLeftRight className="size-4" />
        )}
      </div>

      <div className="min-w-0 flex-1 space-y-0.5">
        <div className="flex flex-wrap items-center gap-1.5">
          <p className="text-sm font-semibold text-foreground/90 group-hover:text-foreground">
            <HighlightMatch text={tx.name} query={nameQuery} />
          </p>
          {tx.tags.map((tag) => (
            <Badge
              key={tag.id}
              variant="secondary"
              className="h-4 px-1.5 py-0 text-[10px] font-medium bg-muted/60"
            >
              {tag.name}
            </Badge>
          ))}
        </div>
        <p className="text-xs text-muted-foreground tabular-nums">
          {formatDate(tx.createdAt)}
        </p>
      </div>

      <p
        className={cn(
          "shrink-0 text-sm font-bold tracking-tight tabular-nums",
          tx.type === "income" && "text-emerald-600 dark:text-emerald-400",
          tx.type === "transfer" && "text-indigo-600 dark:text-indigo-400",
          tx.type === "expense" && "text-rose-600 dark:text-rose-400",
          tx.type === "canceled_income" &&
            "text-muted-foreground line-through opacity-70",
          tx.type === "income_cancellation" &&
            "text-indigo-600 dark:text-indigo-400",
          tx.type === "canceled_expense" &&
            "text-muted-foreground line-through opacity-70",
          tx.type === "expense_cancellation" &&
            "text-indigo-600 dark:text-indigo-400",
        )}
      >
        {tx.type === "income" && "+"}
        {tx.type === "expense" && "−"}
        {tx.type === "income_cancellation" && "↺ "}
        {tx.type === "expense_cancellation" && "↺ "}
        <Money value={tx.amount} />
      </p>

      {tx.type === "income" || tx.type === "expense" ? (
        <RowActions label={`Actions for ${tx.name}`}>
          <DropdownMenuItem
            className="cursor-pointer gap-2"
            onClick={() => onEdit?.(tx.id)}
          >
            <Pencil className="size-4" />
            Edit
          </DropdownMenuItem>
          <DropdownMenuItem
            variant="destructive"
            className="cursor-pointer gap-2 text-destructive focus:text-destructive"
            onClick={() => onDelete?.(tx.id)}
          >
            <Trash2 className="size-4" />
            {tx.type === "income" ? "Cancel income" : "Cancel expense"}
          </DropdownMenuItem>
        </RowActions>
      ) : (
        <div className="size-8 shrink-0" />
      )}
    </DataListRow>
  );
}
