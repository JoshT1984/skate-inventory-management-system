import { HttpInterceptorFn } from '@angular/common/http';

function getCookie(name: string): string | null {
  const match = document.cookie.match(new RegExp(`(^| )${name}=([^;]+)`));
  return match ? decodeURIComponent(match[2]) : null;
}

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const xsrfToken = getCookie('XSRF-TOKEN');
  const cloned = req.clone({
    withCredentials: true,
    setHeaders: xsrfToken ? { 'X-XSRF-TOKEN': xsrfToken } : {},
  });
  return next(cloned);
};
