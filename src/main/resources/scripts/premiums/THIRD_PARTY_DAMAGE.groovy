def premium = riskBasePremium("THIRD_PARTY_DAMAGE") * calculateSumInsuredFactor(riskSumInsured) * calculateRiskCountFactor(riskCount)
return premium
