import { del, get, post, put } from "src/shared/api/client";

export type SavedViewDTO = {
  id: string;
  name: string;
  description: string | null;
  nameFilter: string | null;
  tagIds: string[];
  createdAt: string;
};

export type ViewStatsDTO = {
  totalIncome: number;
  totalExpense: number;
  balance: number;
};

export type MonthStatDTO = {
  month: number;
  income: number;
  expense: number;
  cumulativeNet: number;
};

export const viewsApi = {
  list: () => get<SavedViewDTO[]>("/views"),

  get: (id: string) => get<SavedViewDTO>(`/views/${id}`),

  getStats: (id: string) => get<ViewStatsDTO>(`/views/${id}/stats`),

  getYearStats: (id: string, year: number) =>
    get<MonthStatDTO[]>(`/views/${id}/year-stats`, { year }),

  create: (data: {
    name: string;
    description?: string;
    nameFilter?: string;
    tagIds?: string[];
  }) => post<void>("/views", data),

  update: (
    id: string,
    data: {
      name: string;
      description?: string;
      nameFilter?: string;
      tagIds?: string[];
    },
  ) => put<void>(`/views/${id}`, data),

  delete: (id: string) => del<void>(`/views/${id}`),
};
