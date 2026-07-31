import { ArrowDownLeft, ArrowUpRight, Wallet } from "lucide-react";
import { cn } from "src/shared/lib/utils";
import { Card, CardContent } from "src/shared/ui/card";
import { Money } from "src/shared/ui/money";

type StatCardProps = {
  label: string;
  value: number;
  human?: boolean;
  variant?: "default" | "income" | "expense";
  className?: string;
};

const variantConfig = {
  default: {
    text: "text-foreground",
    bg: "bg-primary/10",
    iconColor: "text-primary",
    Icon: Wallet,
  },
  income: {
    text: "text-income",
    bg: "bg-income/10",
    iconColor: "text-income",
    Icon: ArrowDownLeft,
  },
  expense: {
    text: "text-destructive",
    bg: "bg-destructive/10",
    iconColor: "text-destructive",
    Icon: ArrowUpRight,
  },
};

export function StatCard({
  label,
  value,
  human,
  variant = "default",
  className,
}: StatCardProps) {
  const config = variantConfig[variant];
  const Icon = config.Icon;

  return (
    <Card
      className={cn(
        "overflow-hidden transition-all hover:shadow-md",
        className,
      )}
    >
      <CardContent className="p-5 flex items-center justify-between gap-4">
        <div className="space-y-1.5">
          <p className="text-[12px] font-semibold text-muted-foreground uppercase tracking-widest">
            {label}
          </p>
          <p className={cn("text-2xl font-bold tracking-tight", config.text)}>
            <Money value={value} human={human} />
          </p>
        </div>

        <div
          className={cn(
            "flex size-12 shrink-0 items-center justify-center rounded-full",
            config.bg,
          )}
        >
          <Icon className={cn("size-6", config.iconColor)} />
        </div>
      </CardContent>
    </Card>
  );
}
