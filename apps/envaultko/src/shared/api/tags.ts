import { del, get, post, put } from "src/shared/api/client";

export type TagDTO = { id: string; name: string };

export type PaginatedTagsResponse = {
  items: TagDTO[];
  total: number;
  totalPages: number;
};

export const tagsApi = {
  list: () => get<TagDTO[]>("/tags"),

  listPaged: (page: number, pageSize: number) =>
    get<PaginatedTagsResponse>("/tags/paged", {
      page: String(page),
      pageSize: String(pageSize),
    }),

  create: (data: { name: string }) => post<void>("/tags", data),

  update: (id: string, data: { name: string }) =>
    put<void>(`/tags/${id}`, data),

  delete: (id: string) => del<void>(`/tags/${id}`),
};
