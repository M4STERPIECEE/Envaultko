import { get } from "src/shared/api/client";

export type TransactionDTO = {
  id: string;
  type: string;
  name: string;
  amount: number;
  createdAt: string;
  tags: Array<{ id: string; name: string }>;
};

export type NameSuggestionDTO = {
  name: string;
  tags: Array<{ id: string; name: string }>;
};

export type PaginatedResponse<T> = {
  items: T[];
  total: number;
  totalPages: number;
};

export const transactionsApi = {
  list: (params?: {
    types?: string[];
    name?: string;
    tagIds?: string[];
    page?: number;
  }) =>
    get<PaginatedResponse<TransactionDTO>>(
      "/transactions",
      params as Record<
        string,
        string | number | boolean | string[] | undefined
      >,
    ),

  searchNameSuggestions: (type: string, search?: string) =>
    get<NameSuggestionDTO[]>("/transactions/suggestions", {
      type,
      search: search ?? "",
    } as Record<string, string | number | boolean | string[] | undefined>),
};
