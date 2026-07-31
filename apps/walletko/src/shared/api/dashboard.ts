import { get } from "src/shared/api/client";

export type OverviewStatsDTO = {
  totalBalance: number;
  monthlyIncome: number;
  monthlyExpense: number;
  allTimeIncome: number;
  allTimeExpense: number;
};

export type TopPotDTO = {
  id: string;
  name: string;
  percentage: number;
  color: string;
  isDefault: boolean;
  balance: number;
  createdAt: string;
};

export type MonthStatDTO = {
  month: number;
  income: number;
  expense: number;
  cumulativeNet: number;
};

export const dashboardApi = {
  overview: () => get<OverviewStatsDTO>("/dashboard/overview"),

  topPots: (limit = 4) => get<TopPotDTO[]>("/dashboard/top-pots", { limit }),

  yearStats: (year: number) =>
    get<MonthStatDTO[]>("/dashboard/year-stats", { year }),
};
