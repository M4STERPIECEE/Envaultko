import { parseArgs } from "node:util";

export function parseCliArgs(argv: string[]): {
  financeUrl: string;
  envaultkoUrl: string;
} {
  const { values } = parseArgs({
    args: argv,
    options: {
      finance: { type: "string" },
      envaultko: { type: "string" },
    },
  });

  if (!values.finance || !values.envaultko) {
    throw new Error("Usage: migrate --finance=<db_url> --envaultko=<db_url>");
  }

  return { financeUrl: values.finance, envaultkoUrl: values.envaultko };
}
