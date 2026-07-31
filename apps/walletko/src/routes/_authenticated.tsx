import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";
import { authApi, type UserDTO } from "src/shared/api/auth";
import { AppLayout } from "src/shared/layout/app-layout";

export const Route = createFileRoute("/_authenticated")({
  beforeLoad: async (): Promise<{ user: UserDTO }> => {
    const session = await authApi
      .getSession()
      .catch(() => ({ user: null, session: null }));
    if (!session.user) throw redirect({ to: "/login" });
    return { user: session.user };
  },
  component: () => (
    <AppLayout>
      <Outlet />
    </AppLayout>
  ),
});
