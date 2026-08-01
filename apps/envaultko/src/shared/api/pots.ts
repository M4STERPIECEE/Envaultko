import { get, post, put } from "src/shared/api/client";

export type PotWithBalanceDTO = {
  id: string;
  name: string;
  percentage: number;
  color: string;
  isDefault: boolean;
  createdAt: string;
  balance: number;
};

export type PotTransferRequest = {
  fromPotId: string;
  toPotId: string;
  amount: number;
};

export type AddPotRequest = {
  name: string;
  color: string;
  percentage: number;
  otherPots: Array<{ id: string; percentage: number }>;
};

export type EditAllocationRequest = {
  allPots: Array<{ id: string; percentage: number }>;
};

export type ArchivePotRequest = {
  toPotId?: string;
  remainingPotsPercentages: Array<{ id: string; percentage: number }>;
};

export const potsApi = {
  list: () => get<PotWithBalanceDTO[]>("/pots"),

  getTotalBalance: () => get<{ totalBalance: number }>("/pots/balance"),

  create: (data: AddPotRequest) => post<{ id: string }>("/pots", data),

  edit: (id: string, data: { name: string; color: string }) =>
    put<void>(`/pots/${id}`, data),

  editAllocations: (data: EditAllocationRequest) =>
    put<void>("/pots/allocations", data),

  archive: (id: string, data: ArchivePotRequest) =>
    post<void>(`/pots/${id}/archive`, data),

  transfer: (data: PotTransferRequest) => post<void>("/pots/transfer", data),
};
