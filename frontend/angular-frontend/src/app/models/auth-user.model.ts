export interface AuthUser {
  authenticated: boolean;
  localUserId?: number;
  email?: string;
  firstName?: string;
  lastName?: string;
  roles: string[];
  provider?: string;
}
