import { queryOptions } from "@tanstack/react-query";
import { potsApi } from "src/shared/api/pots";

export const potKeys = {
  all: ["pots"] as const,
  list: () => [...potKeys.all] as const,
  totalBalance: () => [...potKeys.all, "total-balance"] as const,
};

export const potsQuery = queryOptions({
  queryKey: potKeys.list(),
  queryFn: () => potsApi.list(),
});

export const totalBalanceQuery = queryOptions({
  queryKey: potKeys.totalBalance(),
  queryFn: () => potsApi.getTotalBalance(),
});
