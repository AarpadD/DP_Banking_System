package com.example.controller;

import com.example.entities.*;
import com.example.repositories.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class BankController {

    private final BankRepo bankRepo;
    private final ClientRepo clientRepo;
    private final AccountRepo accountRepo;
    private final TransferRepo transferRepo;
    private final InvestmentRepo investmentRepo;

    public BankController(BankRepo bankRepo, ClientRepo clientRepo,
                          AccountRepo accountRepo, TransferRepo transferRepo,
                          InvestmentRepo investmentRepo) {
        this.bankRepo = bankRepo;
        this.clientRepo = clientRepo;
        this.accountRepo = accountRepo;
        this.transferRepo = transferRepo;
        this.investmentRepo = investmentRepo;
    }

    // BANK

    @PostMapping("/banks")
    public ResponseEntity<BankEntity> createBank(@RequestBody Map<String, String> body) {
        String code = body.get("bankCode");
        if (bankRepo.existsById(code))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(bankRepo.save(new BankEntity(code)));
    }

    @GetMapping("/banks")
    public List<BankEntity> getAllBanks() {
        return bankRepo.findAll();
    }

    @GetMapping("/banks/{bankCode}")
    public ResponseEntity<BankEntity> getBank(@PathVariable String bankCode) {
        return bankRepo.findById(bankCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CLIENT

    @PostMapping("/clients")
    public ResponseEntity<ClientEntity> createClient(@RequestBody Map<String, String> body) {
        BankEntity bank = bankRepo.findById(body.get("bankCode")).orElse(null);
        if (bank == null) return ResponseEntity.badRequest().build();

        ClientEntity client = new ClientEntity();
        client.name = body.get("name");
        client.address = body.get("address");
        client.email = body.get("email");
        client.birthday = body.get("birthday");
        client.bank = bank;
        clientRepo.save(client);

        AccountEntity acc = new AccountEntity();
        acc.accCode = body.get("accountCode");
        acc.amount = Double.parseDouble(body.getOrDefault("initialAmount", "0"));
        acc.type = body.getOrDefault("accountType", "RON");
        acc.level = "BASIC";
        acc.client = client;
        accountRepo.save(acc);

        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientEntity> getClient(@PathVariable Long id) {
        return clientRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/clients/{id}/accounts")
    public ResponseEntity<AccountEntity> addAccount(@PathVariable Long id,
                                                     @RequestBody Map<String, String> body) {
        ClientEntity client = clientRepo.findById(id).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        AccountEntity acc = new AccountEntity();
        acc.accCode = body.get("accountCode");
        acc.amount = Double.parseDouble(body.getOrDefault("initialAmount", "0"));
        acc.type = body.getOrDefault("accountType", "RON");
        acc.level = "BASIC";
        acc.client = client;
        return ResponseEntity.status(HttpStatus.CREATED).body(accountRepo.save(acc));
    }

    @DeleteMapping("/clients/{id}/accounts/{accCode}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id, @PathVariable String accCode) {
        AccountEntity acc = accountRepo.findById(accCode).orElse(null);
        if (acc == null || !acc.client.id.equals(id)) return ResponseEntity.notFound().build();
        accountRepo.delete(acc);
        return ResponseEntity.noContent().build();
    }

    // ACCOUNT

    @GetMapping("/accounts/{accCode}")
    public ResponseEntity<AccountEntity> getAccount(@PathVariable String accCode) {
        return accountRepo.findById(accCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/accounts/{accCode}/deposit")
    public ResponseEntity<AccountEntity> deposit(@PathVariable String accCode,
                                                  @RequestBody Map<String, Double> body) {
        AccountEntity acc = accountRepo.findById(accCode).orElse(null);
        if (acc == null) return ResponseEntity.notFound().build();
        acc.amount += body.get("amount");
        return ResponseEntity.ok(accountRepo.save(acc));
    }

    @PostMapping("/accounts/{accCode}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String accCode,
                                       @RequestBody Map<String, Double> body) {
        AccountEntity acc = accountRepo.findById(accCode).orElse(null);
        if (acc == null) return ResponseEntity.notFound().build();
        double amount = body.get("amount");
        if (acc.amount < amount)
            return ResponseEntity.badRequest().body(Map.of("error", "Fonduri insuficiente"));
        acc.amount -= amount;
        return ResponseEntity.ok(accountRepo.save(acc));
    }

    @PostMapping("/accounts/{accCode}/transfer")
    public ResponseEntity<?> transfer(@PathVariable String accCode,
                                       @RequestBody Map<String, Object> body) {
        AccountEntity source = accountRepo.findById(accCode).orElse(null);
        AccountEntity dest = accountRepo.findById((String) body.get("destinationAccCode")).orElse(null);
        if (source == null || dest == null) return ResponseEntity.notFound().build();

        double amount = ((Number) body.get("amount")).doubleValue();
        if (source.amount < amount)
            return ResponseEntity.badRequest().body(Map.of("error", "Fonduri insuficiente"));

        double deposited = source.type.equals(dest.type) ? amount
                : dest.type.equals("EUR") ? amount / 5.0 : amount * 5.0;

        source.amount -= amount;
        dest.amount += deposited;
        accountRepo.save(source);
        accountRepo.save(dest);

        TransferEntity t = new TransferEntity();
        t.transferCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        t.sourceAccount = source;
        t.destinationAccount = dest;
        t.amount = amount;
        t.timestamp = LocalDateTime.now();
        transferRepo.save(t);

        return ResponseEntity.ok(Map.of("transferCode", t.transferCode, "amount", amount));
    }

    @DeleteMapping("/accounts/transfers/{transferCode}")
    public ResponseEntity<?> cancelTransfer(@PathVariable String transferCode) {
        TransferEntity t = transferRepo.findById(transferCode).orElse(null);
        if (t == null) return ResponseEntity.notFound().build();
        if (LocalDateTime.now().isAfter(t.timestamp.plusMinutes(3)))
            return ResponseEntity.badRequest().body(Map.of("error", "Au trecut mai mult de 3 minute"));

        t.sourceAccount.amount += t.amount;
        t.destinationAccount.amount -= t.amount;
        accountRepo.save(t.sourceAccount);
        accountRepo.save(t.destinationAccount);
        transferRepo.delete(t);
        return ResponseEntity.ok(Map.of("message", "Transfer anulat"));
    }

    @PatchMapping("/accounts/{accCode}/upgrade")
    public ResponseEntity<?> upgrade(@PathVariable String accCode,
                                      @RequestBody Map<String, String> body) {
        AccountEntity acc = accountRepo.findById(accCode).orElse(null);
        if (acc == null) return ResponseEntity.notFound().build();
        String level = body.get("level").toUpperCase();
        if (!level.equals("SILVER") && !level.equals("GOLD"))
            return ResponseEntity.badRequest().body(Map.of("error", "Level trebuie sa fie SILVER sau GOLD"));
        acc.level = level;
        return ResponseEntity.ok(accountRepo.save(acc));
    }

    // INVESTMENT

    @PostMapping("/accounts/{accCode}/investments")
    public ResponseEntity<?> makeInvestment(@PathVariable String accCode,
                                             @RequestBody Map<String, Object> body) {
        AccountEntity acc = accountRepo.findById(accCode).orElse(null);
        if (acc == null) return ResponseEntity.notFound().build();
        double amount = ((Number) body.get("amount")).doubleValue();
        if (acc.amount < amount)
            return ResponseEntity.badRequest().body(Map.of("error", "Fonduri insuficiente"));

        acc.amount -= amount;
        accountRepo.save(acc);

        InvestmentEntity inv = new InvestmentEntity();
        inv.asset = (String) body.get("asset");
        inv.investedAmount = amount;
        inv.profit = 0;
        inv.account = acc;
        return ResponseEntity.status(HttpStatus.CREATED).body(investmentRepo.save(inv));
    }

    @GetMapping("/accounts/{accCode}/investments")
    public List<InvestmentEntity> getInvestments(@PathVariable String accCode) {
        return investmentRepo.findByAccount_AccCode(accCode);
    }

    @PostMapping("/accounts/{accCode}/investments/{id}/retrieve")
    public ResponseEntity<?> retrieveInvestment(@PathVariable String accCode,
                                                  @PathVariable Long id,
                                                  @RequestBody Map<String, Double> body) {
        InvestmentEntity inv = investmentRepo.findById(id).orElse(null);
        if (inv == null) return ResponseEntity.notFound().build();

        double profit = body.get("profit");
        double netProfit = profit - profit * 0.10;
        double total = inv.investedAmount + netProfit;

        inv.account.amount += total;
        inv.profit = netProfit;
        accountRepo.save(inv.account);
        investmentRepo.save(inv);

        return ResponseEntity.ok(Map.of(
                "investedAmount", inv.investedAmount,
                "netProfit", netProfit,
                "totalReturned", total
        ));
    }
}