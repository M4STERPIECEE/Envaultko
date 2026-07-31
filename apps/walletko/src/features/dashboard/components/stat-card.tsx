import {
  ArrowDownLeft,
  ArrowUpRight,
  TrendingDown,
  TrendingUp,
  Wallet,
} from "lucide-react";
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
    accentBg: "bg-primary/10 dark:bg-primary/20",
    iconColor: "text-primary",
    borderGlow: "group-hover:border-primary/30",
    glowColor: "from-primary/5 to-transparent",
    Icon: Wallet,
    TrendIcon: TrendingUp,
  },
  income: {
    text: "text-income dark:text-emerald-400",
    accentBg: "bg-income/10 dark:bg-emerald-500/20",
    iconColor: "text-income dark:text-emerald-400",
    borderGlow: "group-hover:border-income/30",
    glowColor: "from-income/5 to-transparent",
    Icon: ArrowDownLeft,
    TrendIcon: TrendingUp,
  },
  expense: {
    text: "text-destructive dark:text-rose-400",
    accentBg: "bg-destructive/10 dark:bg-rose-500/20",
    iconColor: "text-destructive dark:text-rose-400",
    borderGlow: "group-hover:border-destructive/30",
    glowColor: "from-destructive/5 to-transparent",
    Icon: ArrowUpRight,
    TrendIcon: TrendingDown,
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
        "group relative overflow-hidden transition-all duration-300 hover:-translate-y-0.5 hover:shadow-md hover:shadow-primary/5",
        config.borderGlow,
        className,
      )}
    >
      {/* Ambient gradient glow on hover */}
      <div
        className={cn(
          "pointer-events-none absolute -inset-x-0 -top-0 h-24 bg-gradient-to-b opacity-0 transition-opacity duration-300 group-hover:opacity-100",
          config.glowColor,
        )}
      />

      <CardContent className="relative p-5 flex items-center justify-between gap-4">
        <div className="space-y-1.5 min-w-0">
          <p className="text-[11px] font-semibold uppercase tracking-wider text-muted-foreground">
            {label}
          </p>
          <p
            className={cn(
              "text-2xl font-bold tracking-tight tabular-nums truncate",
              config.text,
            )}
          >
            <Money value={value} human={human} />
          </p>
        </div>

        <div
          className={cn(
            "flex size-11 shrink-0 items-center justify-center rounded-xl transition-transform duration-300 group-hover:scale-105",
            config.accentBg,
          )}
        >
          <Icon className={cn("size-5", config.iconColor)} />
        </div>
      </CardContent>
    </Card>
  );
}
