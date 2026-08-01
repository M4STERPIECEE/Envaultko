import { describe, expect, it } from "vitest";
import { parseCliArgs } from "./args";

describe("parseCliArgs", () => {
  it("parses --flag=value form", () => {
    expect(
      parseCliArgs(["--finance=postgres://f", "--envaultko=postgres://w"]),
    ).toEqual({
      financeUrl: "postgres://f",
      envaultkoUrl: "postgres://w",
    });
  });

  it("parses --flag value form", () => {
    expect(
      parseCliArgs([
        "--finance",
        "postgres://f",
        "--envaultko",
        "postgres://w",
      ]),
    ).toEqual({ financeUrl: "postgres://f", envaultkoUrl: "postgres://w" });
  });

  it("throws when --finance is missing", () => {
    expect(() => parseCliArgs(["--envaultko=postgres://w"])).toThrow(/Usage/);
  });

  it("throws when --envaultko is missing", () => {
    expect(() => parseCliArgs(["--finance=postgres://f"])).toThrow(/Usage/);
  });
});
