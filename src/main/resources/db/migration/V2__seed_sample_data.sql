-- =============================================================================
-- V2__seed_sample_data.sql
-- Realistic Seed Data for Cardamom Estate Operations (Idukki High Ranges)
-- =============================================================================

-- 1. Insert Initial Plantation Blocks
INSERT INTO plantation_blocks (name, variety, total_area_acres, planting_year, created_at, updated_at)
VALUES
('Lower Valley Block A', 'VAZHUKKA', 12.50, 2019, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ridge Top Block B', 'MYSORE', 8.25, 2017, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Spring Shade Block C', 'MALABAR', 15.00, 2021, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. Insert Estate Expenses
INSERT INTO plantation_expenses (block_id, category, amount, date, notes, created_at, updated_at)
VALUES
(1, 'LABOR', 18500.00, DATE '2026-08-10', 'Plucking daily wages: 37 worker shifts for second round picking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'FERTILIZER', 34200.00, DATE '2026-08-15', 'Application of 17:17:17 NPK complex and neem cake organic booster', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'PEST_CONTROL', 12800.00, DATE '2026-08-20', 'Targeted organic neem spray for thrips prevention', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(NULL, 'DRYING_FUEL', 26000.00, DATE '2026-09-02', 'Cured firewood procurement for central bhatti kiln drying', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'IRRIGATION', 9500.00, DATE '2026-09-12', 'Micro-sprinkler lateral pipe repairs and pump maintenance', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. Insert Harvest Batches (Green yield & bhatti cured dry weight)
INSERT INTO harvest_batches (block_id, harvest_date, green_weight_kg, dry_weight_kg, bhatti_cost, grade_breakdown_json, status, created_at, updated_at)
VALUES
(1, DATE '2026-08-12', 1250.00, 268.50, 6800.00, '{"AGEB_8mm": 95.0, "AGB_7to8mm": 120.5, "AGS_Superior": 35.0, "Bulk": 18.0}', 'SOLD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, DATE '2026-08-25', 820.00, 172.00, 4600.00, '{"AGEB_8mm": 48.0, "AGB_7to8mm": 84.0, "AGS_Superior": 26.0, "Bulk": 14.0}', 'STORED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, DATE '2026-09-18', 1420.00, NULL, NULL, NULL, 'RAW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4. Insert Auction Sales Transactions
INSERT INTO sale_transactions (batch_id, buyer_or_auction_house, total_quantity_sold_kg, price_per_kg, commission_deduction, net_realization_amount, sale_date, created_at, updated_at)
VALUES
(1, 'Spices Board E-Auction Bodinayakanur', 268.50, 2450.00, 6578.25, 651246.75, DATE '2026-08-28', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
