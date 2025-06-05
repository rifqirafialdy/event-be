package com.example.event_be.refferal.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "cas_app_wallet_tran")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CasAppWalletTran {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "cas_app_wallet_id", length = 50)
    private String casAppWalletId;

    @Column(name = "cas_par_tran_code", length = 20)
    private String tranCode;

    @Column(name = "cas_par_tran_book_type_code", length = 20)
    private String bookTypeCode;

    @Column(name = "to_reference_number", length = 40)
    private String toReferenceNumber;

    @Column(name = "cas_par_tran_amount", precision = 15, scale = 3)
    private java.math.BigDecimal amount;

    @Column(name = "amount_expired")
    private boolean amountExpired = false;

    @Column(name = "amount_expired_at")
    private ZonedDateTime amountExpiredAt;

}

