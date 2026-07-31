import { queryOptions } from "@tanstack/react-query";
import { viewsApi } from "src/shared/api/views";

export const viewKeys = {
  all: ["views"] as const,
  list: () => [...viewKeys.all] as const,
  detail: (id: string) => [...viewKeys.all, id] as const,
  stats: (id: string) => [...viewKeys.all, "stats", id] as const,
  yearStats: (id: string, year?: number) =>
    year !== undefined
      ? ([...viewKeys.all, "year-stats", id, year] as const)
      : ([...viewKeys.all, "year-stats", id] as const),
};

export const viewsQuery = queryOptions({
  queryKey: viewKeys.list(),
  queryFn: () => viewsApi.list(),
});

export const viewQuery = (id: string) =>
  queryOptions({
    queryKey: viewKeys.detail(id),
    queryFn: () => viewsApi.get(id),
  });

export const viewStatsQuery = (id: string) =>
  queryOptions({
    queryKey: viewKeys.stats(id),
    queryFn: () => viewsApi.getStats(id),
  });

export const viewYearStatsQuery = (id: string, year: number) =>
  queryOptions({
    queryKey: viewKeys.yearStats(id, year),
    queryFn: () => viewsApi.getYearStats(id, year),
  });
