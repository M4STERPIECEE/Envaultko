import {
  PostgreSqlContainer,
  type StartedPostgreSqlContainer,
} from "@testcontainers/postgresql";
import pg from "pg";
import { afterAll, beforeAll, describe, expect, it } from "vitest";
import {
  applyFinanceSchema,
  applyEnvaultkoSchema,
  seedFinance,
  type SeedResult,
} from "./test-helpers";

let financeContainer: StartedPostgreSqlContainer;
let envaultkoContainer: StartedPostgreSqlContainer;
let finance: pg.Client;
let envaultko: pg.Client;
let seed: SeedResult;

beforeAll(async () => {
  [financeContainer, envaultkoContainer] = await Promise.all([
    new PostgreSqlContainer("postgres:16-alpine").start(),
    new PostgreSqlContainer("postgres:16-alpine").start(),
  ]);
  finance = new pg.Client({
    connectionString: financeContainer.getConnectionUri(),
  });
  envaultko = new pg.Client({
    connectionString: envaultkoContainer.getConnectionUri(),
  });
  await finance.connect();
  await envaultko.connect();
  await applyFinanceSchema(finance);
  await applyEnvaultkoSchema(envaultko);
  seed = await seedFinance(finance);
}, 120_000);

afterAll(async () => {
  await finance?.end();
  await envaultko?.end();
  await Promise.all([financeContainer?.stop(), envaultkoContainer?.stop()]);
});

describe("verification harness", () => {
  it("seeds finance with users, tags, and operations", async () => {
    expect(seed.userIds.length).toBe(4);
    const userCount = await finance.query<{ n: number }>(
      `SELECT COUNT(*)::int AS n FROM "User"`,
    );
    expect(userCount.rows[0].n).toBe(4);
    const opCount = await finance.query<{ n: number }>(
      `SELECT COUNT(*)::int AS n FROM "Operation"`,
    );
    expect(opCount.rows[0].n).toBe(seed.operations.length);
  });

  it("applies the envaultko schema to an empty target", async () => {
    const userCount = await envaultko.query<{ n: number }>(
      `SELECT COUNT(*)::int AS n FROM "user"`,
    );
    expect(userCount.rows[0].n).toBe(0);
    const potCount = await envaultko.query<{ n: number }>(
      `SELECT COUNT(*)::int AS n FROM pots`,
    );
    expect(potCount.rows[0].n).toBe(0);
  });
});
