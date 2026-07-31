import { keepPreviousData, queryOptions } from "@tanstack/react-query";

type SuggestibleTransactionType = "income" | "expense";

import { expenseApi } from "src/shared/api/expense";
import { incomeApi } from "src/shared/api/income";
import { transactionsApi } from "src/shared/api/transactions";

type TransactionFilters = {
  types: string[];
  name: string;
  tagIds: string[];
  page: number;
};

export const transactionKeys = {
  all: ["transactions"] as const,
  list: (filters: TransactionFilters) =>
    [...transactionKeys.all, "list", filters] as const,
  income: (id: string) => [...transactionKeys.all, "income", id] as const,
  expense: (id: string) => [...transactionKeys.all, "expense", id] as const,
  expenseCancelPreview: (id: string) =>
    [...transactionKeys.all, "expense-cancel-preview", id] as const,
  incomeCancelPreview: (id: string) =>
    [...transactionKeys.all, "income-cancel-preview", id] as const,
  nameSuggestions: (type: SuggestibleTransactionType, search: string) =>
    [...transactionKeys.all, "name-suggestions", type, search] as const,
};

export const transactionsQuery = (filters: TransactionFilters) =>
  queryOptions({
    queryKey: transactionKeys.list(filters),
    queryFn: () =>
      transactionsApi.list({
        types: filters.types.length > 0 ? filters.types : undefined,
        name: filters.name || undefined,
        tagIds: filters.tagIds.length > 0 ? filters.tagIds : undefined,
        page: filters.page,
      }),
  });

export const incomeQuery = (id: string) =>
  queryOptions({
    queryKey: transactionKeys.income(id),
    queryFn: () => incomeApi.get(id),
  });

export const expenseQuery = (id: string) =>
  queryOptions({
    queryKey: transactionKeys.expense(id),
    queryFn: () => expenseApi.get(id),
  });

export const expenseCancelPreviewQuery = (id: string) =>
  queryOptions({
    queryKey: transactionKeys.expenseCancelPreview(id),
    queryFn: () => expenseApi.getCancelPreview(id),
  });

export const incomeCancelPreviewQuery = (id: string) =>
  queryOptions({
    queryKey: transactionKeys.incomeCancelPreview(id),
    queryFn: () => incomeApi.getCancelPreview(id),
  });

export const nameSuggestionsQuery = (
  type: SuggestibleTransactionType,
  search: string,
) =>
  queryOptions({
    queryKey: transactionKeys.nameSuggestions(type, search),
    queryFn: () => transactionsApi.searchNameSuggestions(type, search),
    placeholderData: keepPreviousData,
    staleTime: 30_000,
    retry: false,
  });
