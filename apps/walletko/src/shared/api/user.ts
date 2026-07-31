import { put } from "src/shared/api/client";

export const userApi = {
  updateName: (data: { name: string }) => put<void>("/user/name", data),
};
