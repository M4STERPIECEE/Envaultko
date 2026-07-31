import { queryOptions } from "@tanstack/react-query";
import { dashboardApi } from "src/shared/api/dashboard";

export const dashboardKeys = {
  all: ["dashboard"] as const,
  overview: () => [...dashboardKeys.all, "overview"] as const,
  topPots: (limit: number) =>
    [...dashboardKeys.all, "top-pots", limit] as const,
  yearStats: (year: number) =>
    [...dashboardKeys.all, "year-stats", year] as const,
};

export const overviewStatsQuery = queryOptions({
  queryKey: dashboardKeys.overview(),
  queryFn: () => dashboardApi.overview(),
});

export const topPotsQuery = (limit = 4) =>
  queryOptions({
    queryKey: dashboardKeys.topPots(limit),
    queryFn: () => dashboardApi.topPots(limit),
  });

export const yearStatsQuery = (year: number) =>
  queryOptions({
    queryKey: dashboardKeys.yearStats(year),
    queryFn: () => dashboardApi.yearStats(year),
  });
