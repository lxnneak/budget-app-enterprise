import React, { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { apiFetch } from "../services/api"
import type {
  Transaction,
  CreateTransactionRequest,
  TransactionType,
} from "../types/transaction"

const DashboardPage: React.FC = () => {
  const navigate = useNavigate()

  const [transactions, setTransactions] = useState<Transaction[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string>("")

  const [description, setDescription] = useState("")
  const [category, setCategory] = useState("")
  const [amount, setAmount] = useState<number | "">("")
  const [date, setDate] = useState(new Date().toISOString().split("T")[0])
  const [type, setType] = useState<TransactionType>("EXPENSE")
  const [balance, setBalance] = useState<number | null>(null)

  const fetchTransactions = async () => {
    try {
      setLoading(true)
      setError("")
      const data = await apiFetch<Transaction[]>("/transactions")
      setTransactions(data)
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError("Kunde inte hämta transaktioner.")
      }
    } finally {
      setLoading(false)
    }
  }

  const fetchBalance = async () => {
    try {
      const data = await apiFetch<number>("/transactions/balance")
      setBalance(data)
    } catch (err) {
      console.error("Kunde inte hämta saldo:", err)
    }
  }

  const fetchAllData = async () => {
    await fetchTransactions()
    await fetchBalance()
  }

  useEffect(() => {
    fetchAllData()
  }, [])

  const handleCreateTransaction = async (e: React.SubmitEvent) => {
    e.preventDefault()
    if (!amount || amount <= 0) {
      alert("Ange ett giltigt belopp!")
      return
    }

    const newTransaction: CreateTransactionRequest = {
      type,
      amount: Number(amount),
      category: category.trim() || undefined,
      description: description.trim() || undefined,
      date,
    }

    try {
      await apiFetch<Transaction>("/transactions", {
        method: "POST",
        body: JSON.stringify(newTransaction),
      })

      setDescription("")
      setCategory("")
      setAmount("")
      fetchAllData()
    } catch (err: unknown) {
      if (err instanceof Error) {
        alert(`Kunde inte skapa transaktion: ${err.message}`)
      }
    }
  }

  const handleDeleteTransaction = async (id: string) => {
    if (!window.confirm("Är du säker på att du vill ta bort transaktionen?"))
      return

    try {
      await apiFetch(`/transactions/${id}`, {
        method: "DELETE",
      })

      setTransactions((prev) => prev.filter((t) => t.id !== id))
      fetchBalance()
    } catch (err: unknown) {
      if (err instanceof Error) {
        alert(`Kunde inte ta bort: ${err.message}`)
      }
    }
  }

  const handleLogout = async () => {
    try {
      await apiFetch("/auth/logout", { method: "POST" })
    } catch (err) {
      console.error("Logout-fel:", err)
    } finally {
      navigate("/login")
    }
  }

  return (
    <div className="container">
      <header className="header">
        <h2>Min Budget Dashboard</h2>
        <button onClick={handleLogout} className="btn-secondary">
          Logga ut
        </button>
      </header>

      <div className="card">
        <h3>Lägg till ny transaktion</h3>
        <form onSubmit={handleCreateTransaction}>
          <div className="form-row">
            <div>
              <label>Kategori (frivilligt):</label>
              <input
                type="text"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                placeholder="ex Mat, Hyra, Nöje..."
                maxLength={100}
              />
            </div>

            <div>
              <label>Beskrivning (frivilligt):</label>
              <input
                type="text"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="ex Matinköp, Restaurang, Bio..."
                maxLength={255}
              />
            </div>
          </div>

          <div className="form-row">
            <div>
              <label>Belopp (SEK):</label>
              <input
                type="number"
                step="0.01"
                value={amount}
                onChange={(e) =>
                  setAmount(e.target.value ? Number(e.target.value) : "")
                }
                required
                placeholder="0.00"
              />
            </div>

            <div>
              <label>Typ:</label>
              <select
                value={type}
                onChange={(e) => setType(e.target.value as TransactionType)}
              >
                <option value="EXPENSE">Utgift</option>
                <option value="INCOME">Inkomst</option>
              </select>
            </div>

            <div>
              <label>Datum:</label>
              <input
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                required
              />
            </div>
          </div>

          <button type="submit">Spara transaktion</button>
        </form>
      </div>

      <div className="card">
        <h3>Transaktioner</h3>
        {error && <p className="error-message">{error}</p>}

        {loading ? (
          <p>Laddar transaktioner...</p>
        ) : transactions.length === 0 ? (
          <p>Inga transaktioner hittades. Skapa en ovan!</p>
        ) : (
          <>
            <table>
              <thead>
                <tr>
                  <th>Datum</th>
                  <th>Kategori</th>
                  <th>Beskrivning</th>
                  <th>Belopp</th>
                  <th>Åtgärd</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((t) => (
                  <tr key={t.id}>
                    <td>{t.date}</td>
                    <td>{t.category || "-"}</td>
                    <td>{t.description || "-"}</td>
                    <td
                      className={
                        t.type === "INCOME" ? "amount-income" : "amount-expense"
                      }
                    >
                      {t.type === "INCOME" ? `+${t.amount}` : `-${t.amount}`} kr
                    </td>
                    <td>
                      <button
                        onClick={() => handleDeleteTransaction(t.id)}
                        className="btn-danger"
                      >
                        Ta bort
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {balance !== null && (
              <div className="balance-summary">
                <strong>Totalt saldo:</strong>{" "}
                <span
                  className={balance >= 0 ? "amount-income" : "amount-expense"}
                >
                  {balance} kr
                </span>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  )
}

export default DashboardPage
