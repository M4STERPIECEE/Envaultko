import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { dashboardKeys } from "src/features/dashboard/queries";
import { potKeys, potsQuery } from "src/features/pots/queries";
import {
  incomeCancelPreviewQuery,
  transactionKeys,
} from "src/features/transactions/queries";
import { incomeApi } from "src/shared/api/income";
import { Alert, AlertDescription } from "src/shared/ui/alert";
import { Button } from "src/shared/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "src/shared/ui/dialog";
import { Money } from "src/shared/ui/money";
import { Spinner } from "src/shared/ui/spinner";

type CancelIncomeDialogProps = {
  income: { id: string; name: string } | null;
  onOpenChange: (open: boolean) => void;
};

export function CancelIncomeDialog({
  income,
  onOpenChange,
}: CancelIncomeDialogProps) {
  const qc = useQueryClient();

  const {
    data: preview,
    isLoading,
    isError,
  } = useQuery({
    ...incomeCancelPreviewQuery(income?.id ?? ""),
    enabled: income !== null,
  });

  const { data: pots } = useQuery(potsQuery);

  const mutation = useMutation({
    mutationFn: (args: { id: string }) => incomeApi.cancel(args.id),
    onSuccess: (result) => {
      if (result.blocked) {
        if (income) {
          qc.invalidateQueries({
            queryKey: transactionKeys.incomeCancelPreview(income.id),
          });
        }
        return;
      }
      qc.invalidateQueries({ queryKey: transactionKeys.all });
      qc.invalidateQueries({ queryKey: dashboardKeys.all });
      qc.invalidateQueries({ queryKey: potKeys.all });
      onOpenChange(false);
    },
  });

  const handleClose = () => {
    mutation.reset();
    onOpenChange(false);
  };

  const potNameMap = new Map((pots ?? []).map((p) => [p.id, p.name]));

  const previewLines = (preview?.allocations ?? []).map(
    (alloc: { potId: string; amount: number }) => {
      const name = potNameMap.get(alloc.potId) ?? alloc.potId;
      return { potId: alloc.potId, potName: name, amount: alloc.amount };
    },
  );

  const previewReady = !isLoading && !isError && preview != null;

  return (
    <Dialog open={income !== null} onOpenChange={(v) => !v && handleClose()}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Cancel income</DialogTitle>
          <DialogDescription>
            The money this income added will be taken back from each pot.
          </DialogDescription>
        </DialogHeader>

        {isLoading ? (
          <div className="flex justify-center py-6">
            <Spinner className="size-5 text-primary" />
          </div>
        ) : !previewReady ? (
          <Alert variant="destructive">
            <AlertDescription>
              Failed to load the cancellation details. Please try again.
            </AlertDescription>
          </Alert>
        ) : (
          <div className="space-y-2">
            <ul className="divide-y rounded-md border">
              {previewLines.map((line) => (
                <li
                  key={line.potId}
                  className="flex items-center justify-between gap-2 px-3 py-2 text-sm"
                >
                  <span>{line.potName}</span>
                  <span className="flex items-center gap-2 shrink-0 tabular-nums">
                    <span className="font-medium text-destructive">
                      −<Money value={line.amount} />
                    </span>
                    {resultText()}
                  </span>
                </li>
              ))}
            </ul>
          </div>
        )}

        {mutation.isError && (
          <Alert variant="destructive">
            <AlertDescription>
              Something went wrong. Please try again.
            </AlertDescription>
          </Alert>
        )}

        <DialogFooter>
          <Button
            variant="outline"
            onClick={handleClose}
            disabled={mutation.isPending}
          >
            Keep income
          </Button>
          <Button
            variant="destructive"
            onClick={() => income && mutation.mutate({ id: income.id })}
            disabled={mutation.isPending || !previewReady}
          >
            {mutation.isPending ? "Cancelling…" : "Cancel income"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

const resultText = () => <span className="text-muted-foreground">removed</span>;
