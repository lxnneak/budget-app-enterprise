const BASE_URL = "http://localhost:8080/api"

export const apiFetch = async <T>(
  endpoint: string,
  options: RequestInit = {},
): Promise<T> => {
  const config: RequestInit = {
    ...options,
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
  }

  const response = await fetch(`${BASE_URL}${endpoint}`, config)

  if (!response.ok) {
    const errorData = await response
      .json()
      .catch(() => ({ error: "An error occurred" }))
    throw new Error(errorData.error || errorData.message || "Server error")
  }

  if (response.status === 204) {
    return {} as T
  }

  return response.json()
}
