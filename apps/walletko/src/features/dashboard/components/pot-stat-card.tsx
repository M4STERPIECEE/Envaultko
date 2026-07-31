import { cn } from "src/shared/lib/utils";
import { Card, CardContent } from "src/shared/ui/card";
import { Money } from "src/shared/ui/money";
import {
  Progress,
  ProgressIndicator,
  ProgressTrack,
} from "src/shared/ui/progress";

type PotStatCardProps = {
  name: string;
  balance: number;
  color: string;
  percentage: number;
  className?: string;
};

export function PotStatCard({
  name,
  balance,
  color,
  percentage,
  className,
}: PotStatCardProps) {
  return (
    <Card
      className={cn(
        "group relative overflow-hidden transition-all duration-300 hover:-translate-y-0.5 hover:shadow-md hover:shadow-primary/5",
        className,
      )}
    >
      {/* Top accent line matching pot color */}
      <div
        className="absolute top-0 inset-x-0 h-1 transition-all duration-300 group-hover:h-1.5 opacity-80"
        style={{ backgroundColor: color }}
      />

      <CardContent className="pt-4 pb-4 px-5 space-y-3">
        <div className="flex items-center justify-between gap-2">
          <div className="flex items-center gap-2 min-w-0">
            <span
              className="size-2.5 rounded-full shrink-0 shadow-sm"
              style={{ backgroundColor: color }}
            />
            <p className="text-sm font-semibold truncate text-foreground/90 group-hover:text-foreground transition-colors">
              {name}
            </p>
          </div>
          <span className="inline-flex items-center rounded-full bg-muted/60 px-2 py-0.5 text-xs font-semibold text-muted-foreground tabular-nums shrink-0">
            {percentage}%
          </span>
        </div>

        <p className="text-xl font-bold tracking-tight tabular-nums">
          <Money value={balance} />
        </p>

        <Progress value={percentage} className="h-1.5">
          <ProgressTrack className="bg-muted/50">
            <ProgressIndicator
              className="transition-all duration-500 rounded-full"
              style={{ backgroundColor: color }}
            />
          </ProgressTrack>
        </Progress>
      </CardContent>
    </Card>
  );
}
