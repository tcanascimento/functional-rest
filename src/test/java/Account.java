public class Account {

    private String accountNumber;
    private String accountType;
    private String bankBranch;

    public Account(String accountNumber, String accountType, String bankBranch) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.bankBranch = bankBranch;
    }

    public Account() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", bankBranch='" + bankBranch + '\'' +
                '}';
    }
}
