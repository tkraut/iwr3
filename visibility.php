<?php
// pro vsechna policka napocitame viditelnost
$mlb = $this->getMaxLookoutBonus();
$vis_len = $mlb+1;
foreach ($wdata as $key=>$policko) {
	$fld_vis[$policko['idfld']]['vis'] = false;
	$fld_vis[$policko['idfld']]['cnt'] = array(); // kolik lidi pole vidi
	$fld_vis[$policko['idfld']]['my'] = $policko['field_owner'];
	$fld_vis[$policko['idfld']]['aly'] = $this->isFieldOwnedByAly($policko['field_owner']);;
}
foreach ($fld_vis as $key=>$val) {
	$x = $key % $ww;
	$y = floor($key / $ww);
	$od_x = max($x - $vis_len, 0);
	$do_x = min($x + $vis_len, $ww);
	$od_y = max($y - $vis_len, 0);
	$do_y = min($y + $vis_len, $wh);
	// projdem ctverec okruhu viditelnosti
	for ($i=$od_x; $i<=$do_x; $i++) {
		for ($j=$od_y; $j<=$do_y; $j++) {
			$nid = $j * $ww + $i; // nove ID pri posunu
			if ($nid>=($j+1)*$ww) continue; // jsme-li mimo mapu, jdem dal
			if (isset($fld_vis[$nid]) && ($fld_vis[$nid]['my']==$uid || $fld_vis[$nid]['aly'])) {
				$fld_vis[$key]['vis'] = true; /// tady by to asi melo bejt naopak
			}
			$ci = $fld_vis[$key]['my'];
			if ($ci) {
				if (!isset($fld_vis[$nid]['cnt'][$ci])) $fld_vis[$nid]['cnt'][$ci] = 0;
				$fld_vis[$nid]['cnt'][$ci]++; // napocitame viditelnost
			}
		}
	}
}