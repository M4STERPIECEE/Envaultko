# syntax=docker/dockerfile:1

FROM node:22-alpine AS base
ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME:$PATH"
RUN corepack enable

FROM base AS build
WORKDIR /app
COPY . .
RUN --mount=type=cache,id=pnpm,target=/pnpm/store \
    pnpm install --frozen-lockfile --filter finance-to-envaultko...
RUN pnpm --filter finance-to-envaultko build

FROM gcr.io/distroless/nodejs22-debian12 AS runner
WORKDIR /app
COPY --from=build /app/apps/finance-to-envaultko/dist/cli.cjs ./cli.cjs
ENTRYPOINT ["/nodejs/bin/node", "/app/cli.cjs"]
