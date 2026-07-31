export type TransactionType = "INCOME" | "EXPENSE"

export interface Transaction {
  id: string
  type: TransactionType
  amount: number
  category?: string
  description?: string
  date: string
}

export interface CreateTransactionRequest {
  type: TransactionType
  amount: number
  category?: string
  description?: string
  date: string
}
