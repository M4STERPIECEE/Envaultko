import { get, post, put } from "src/shared/api/client";
import type { TagInput } from "src/shared/api/income";

export type ExpenseDTO = {
  id: string;
  name: string;
  amount: number;
  createdAt: string;
  tags: Array<{ id: string; name: string }>;
  allocations: Array<{ potId: string; amount: number }>;
};

export const expenseApi = {
  pay: (data: {
    name: string;
    tags: TagInput[];
    drawFrom: Array<{ potId: string; amount: number }>;
    createdAt?: string;
  }) => post<{ id: string }>("/expenses", data),

  get: (id: string) => get<ExpenseDTO>(`/expenses/${id}`),

  update: (
    id: string,
    data: { name: string; date: string; tags: TagInput[] },
  ) => put<{ id: string }>(`/expenses/${id}`, data),

  cancel: (id: string) => post<{ id: string }>(`/expenses/${id}/cancel`),

  getCancelPreview: (id: string) =>
    get<{ allocations: Array<{ potId: string; amount: number }> }>(
      `/expenses/${id}/cancel-preview`,
    ),
};
