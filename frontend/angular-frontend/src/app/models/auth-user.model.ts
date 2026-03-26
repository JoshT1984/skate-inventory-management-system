export interface AuthUser {
  authenticated: boolean;
  localUserId: number | null;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
  provider: string;
}
