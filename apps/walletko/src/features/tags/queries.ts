import { queryOptions } from "@tanstack/react-query";
import { tagsApi } from "src/shared/api/tags";

export const TAGS_PAGE_SIZE = 20;

export const tagKeys = {
  all: ["tags"] as const,
  list: () => [...tagKeys.all] as const,
  paged: (page: number, pageSize: number) =>
    [...tagKeys.all, "paged", page, pageSize] as const,
};

export const tagsQuery = queryOptions({
  queryKey: tagKeys.list(),
  queryFn: () => tagsApi.list(),
});

export const tagsPagedQuery = (page: number, pageSize: number) =>
  queryOptions({
    queryKey: tagKeys.paged(page, pageSize),
    queryFn: () => tagsApi.listPaged(page, pageSize),
  });
