import { get, post } from "src/shared/api/client";

export type UserDTO = {
  id: string;
  name: string;
  email: string;
  emailVerified?: boolean;
};

export type SessionDTO = {
  user: UserDTO | null;
  session: { id: string } | null;
};

export const authApi = {
  sendOtp: (email: string) =>
    post<{ message: string }>("/auth/email-otp/send-verification-otp", {
      email,
    }),

  signIn: (email: string, otp: string) =>
    post<{ user: UserDTO; session: { id: string } }>(
      "/auth/sign-in/email-otp",
      { email, otp },
    ),

  signOut: () => post<void>("/auth/sign-out"),

  getSession: () => get<SessionDTO>("/auth/session"),

  me: () => get<UserDTO>("/auth/me"),
};
