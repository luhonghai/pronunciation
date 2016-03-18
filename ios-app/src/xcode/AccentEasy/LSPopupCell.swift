//
//  LSPopupCell.swift
//  AccentEasy
//
//  Created by CMGVN on 3/15/16.
//  Copyright © 2016 Claybourne McGregor Consulting Ltd (CMG Ltd). All rights reserved.
//

import UIKit

class LSPopupCell: UITableViewCell {
    
    @IBOutlet weak var cellFlag: UIImageView!
    @IBOutlet weak var cellCountryName: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        //print("selected cell")

        // Configure the view for the selected state
    }
    
    override func layoutSubviews() {
        //self.cellCountryName.preferredMaxLayoutWidth = self.cellCountryName.frame.size.width
    }
    
}
