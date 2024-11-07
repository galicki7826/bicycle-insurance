def premium = riskBasePremium("DAMAGE") * calculateSumInsuredFactor(riskSumInsured) * calculateAgeFactor(make, model, bicycleAge)
return premium
