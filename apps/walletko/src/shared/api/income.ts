import { get, post, put } from "src/shared/api/client";

export type TagInput = { id?: string | null; name: string };

export type IncomeDTO = {
  id: string;
  name: string;
  amount: number;
  createdAt: string;
  tags: Array<{ id: string; name: string }>;
};

export const incomeApi = {
  receive: (data: {
    name: string;
    amount: number;
    tags: TagInput[];
    createdAt?: string;
  }) => post<{ id: string }>("/income", data),

  get: (id: string) => get<IncomeDTO>(`/income/${id}`),

  update: (
    id: string,
    data: { name: string; date: string; tags: TagInput[] },
  ) => put<{ id: string }>(`/income/${id}`, data),

  cancel: (id: string) =>
    post<
      | { blocked: false }
      | {
          blocked: true;
          code: string;
          pots: Array<{ name: string; shortfall: number }>;
        }
    >(`/income/${id}/cancel`),

  getCancelPreview: (id: string) =>
    get<{ allocations: Array<{ potId: string; amount: number }> }>(
      `/income/${id}/cancel-preview`,
    ),
};
