import { get } from "src/shared/api/client";

export type AppMetaDTO = {
  version: string;
  releaseDate: string | null;
};

export const metaApi = {
  get: () => get<AppMetaDTO>("/meta"),
};
