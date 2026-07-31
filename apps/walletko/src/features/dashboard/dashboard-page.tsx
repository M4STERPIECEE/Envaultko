import {
  keepPreviousData,
  useQuery,
  useSuspenseQuery,
} from "@tanstack/react-query";
import { ArrowDownLeft, ArrowUpRight, PlusIcon, Settings } from "lucide-react";
import { useState } from "react";
import { PotStatCard } from "src/features/dashboard/components/pot-stat-card";
import { StatCard } from "src/features/dashboard/components/stat-card";
import { YearlyChart } from "src/features/dashboard/components/yearly-chart";
import {
  overviewStatsQuery,
  topPotsQuery,
  yearStatsQuery,
} from "src/features/dashboard/queries";
import { AddExpenseDialog } from "src/features/transactions/components/add-expense-dialog";
import { AddIncomeDialog } from "src/features/transactions/components/add-income-dialog";
import {
  STAT_KEYS,
  useStatVisibility,
} from "src/shared/hooks/use-stat-visibility";
import {
  PageContent,
  PageHeader,
  SectionHeading,
} from "src/shared/layout/page";
import { Button } from "src/shared/ui/button";
import { PageActions } from "src/shared/ui/page-actions";
import { Popover, PopoverContent, PopoverTrigger } from "src/shared/ui/popover";
import { Switch } from "src/shared/ui/switch";

const user = { name: "Hery Nirintsoa" };

export function DashboardPage() {
  const { visible, toggle, labels } = useStatVisibility();
  const [addIncomeOpen, setAddIncomeOpen] = useState(false);
  const [addExpenseOpen, setAddExpenseOpen] = useState(false);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

  const { data: overview } = useSuspenseQuery(overviewStatsQuery);
  const { data: topPots } = useSuspenseQuery(topPotsQuery());
  const { data: yearStats } = useQuery({
    ...yearStatsQuery(selectedYear),
    placeholderData: keepPreviousData,
  });

  return (
    <PageContent className="space-y-8 pb-12">
      <PageHeader
        eyebrow="Welcome back,"
        title={user.name}
        action={
          <PageActions
            primary={{
              key: "add",
              label: "New Transaction",
              icon: <PlusIcon className="size-4" />,
              onClick: () => {},
              items: [
                {
                  key: "income",
                  label: "Add Income",
                  icon: <ArrowDownLeft className="size-4 text-income" />,
                  onClick: () => setAddIncomeOpen(true),
                },
                {
                  key: "expense",
                  label: "Add Expense",
                  icon: <ArrowUpRight className="size-4 text-destructive" />,
                  onClick: () => setAddExpenseOpen(true),
                },
              ],
            }}
          />
        }
      />

      <SectionHeading
        title="Overview"
        action={
          <Popover>
            <PopoverTrigger
              render={
                <Button
                  variant="outline"
                  size="sm"
                  className="h-8 gap-1.5 text-xs font-medium cursor-pointer text-muted-foreground hover:text-foreground"
                  aria-label="Customize visible stats"
                >
                  <Settings className="size-3.5" />
                  <span>Customize</span>
                </Button>
              }
            />
            <PopoverContent
              side="bottom"
              align="end"
              className="w-64 p-3 shadow-xl rounded-xl"
            >
              <div className="space-y-1 mb-2 px-1">
                <p className="text-xs font-bold uppercase text-muted-foreground tracking-wider">
                  Stat Visibility
                </p>
              </div>
              <div className="space-y-2">
                {STAT_KEYS.map((key) => (
                  <div
                    key={key}
                    className="flex items-center justify-between gap-2 p-1.5 rounded-lg hover:bg-muted/50 transition-colors"
                  >
                    <span className="text-xs font-medium">{labels[key]}</span>
                    <Switch
                      checked={visible.has(key)}
                      onCheckedChange={() => toggle(key)}
                      aria-label={labels[key]}
                    />
                  </div>
                ))}
              </div>
            </PopoverContent>
          </Popover>
        }
      >
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-4">
          {visible.has("totalBalance") && (
            <StatCard
              label="Total Balance"
              value={overview.totalBalance}
              className="md:col-span-2"
            />
          )}
          {visible.has("monthIncome") && (
            <StatCard
              label="Income this month"
              value={overview.monthlyIncome}
              variant="income"
              className="lg:col-span-2"
            />
          )}
          {visible.has("monthExpense") && (
            <StatCard
              label="Expenses this month"
              value={overview.monthlyExpense}
              variant="expense"
              className="lg:col-span-2"
            />
          )}
          {visible.has("allTimeIncome") && (
            <StatCard
              label="All Time Income"
              value={overview.allTimeIncome}
              human
              variant="income"
              className="lg:col-span-2"
            />
          )}
          {visible.has("allTimeExpense") && (
            <StatCard
              label="All Time Expense"
              value={overview.allTimeExpense}
              human
              variant="expense"
              className="lg:col-span-2"
            />
          )}
        </div>
      </SectionHeading>

      {topPots.length > 0 && (
        <SectionHeading title="Top Pots">
          <div className="lg:hidden flex gap-3 overflow-x-auto pb-2 snap-x snap-mandatory scrollbar-none">
            {topPots.map((pot) => (
              <div key={pot.id} className="min-w-[200px] snap-start">
                <PotStatCard
                  name={pot.name}
                  balance={pot.balance}
                  color={pot.color}
                  percentage={pot.percentage}
                />
              </div>
            ))}
          </div>
          <div className="hidden lg:grid grid-cols-4 gap-4">
            {topPots.map((pot) => (
              <PotStatCard
                key={pot.id}
                name={pot.name}
                balance={pot.balance}
                color={pot.color}
                percentage={pot.percentage}
              />
            ))}
          </div>
        </SectionHeading>
      )}

      {yearStats && (
        <YearlyChart
          data={yearStats}
          year={selectedYear}
          availableYears={[selectedYear - 1, selectedYear, selectedYear + 1]}
          onYearChange={setSelectedYear}
        />
      )}

      <AddIncomeDialog open={addIncomeOpen} onOpenChange={setAddIncomeOpen} />
      <AddExpenseDialog
        open={addExpenseOpen}
        onOpenChange={setAddExpenseOpen}
      />
    </PageContent>
  );
}
